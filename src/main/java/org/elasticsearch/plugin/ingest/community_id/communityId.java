/*
 * Licensed to Elasticsearch under one or more contributor
 * license agreements. See the NOTICE file distributed with
 * this work for additional information regarding copyright
 * ownership. Elasticsearch licenses this file to you under
 * the Apache License, Version 2.0 (the "License"); you may
 * not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.elasticsearch.plugin.ingest.community_id;

import java.math.BigInteger;
import java.net.InetAddress;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;



public class communityId {

    // seed for communityId calculation (default 0)
    // boolean for base64 encoded (default true)
    // padding for digest (default 0)
    private final int seed;
    private final boolean encoded;
    private final String hash;
    private final byte pad = 0;


    /**
     * Default constructor for the communityId object: SHA1, Base64, 0 Seed and Padding.
     */
    public communityId() {
        this.seed = 0;
        this.encoded = true;
        this.hash = "SHA-1";
    }

    /**
     * CommunityId Object constructor. 
     *
     * The community ID String returned will be either hex encoded or base64 encoded depending
     * on the generators configuration. Setting the second parameter to true turns on base64 encoding.
     *
     * Only the first two bytes of the seed are used in the creation of the ID.
     *
     * @param seed    Integer Seed value per Community_ID Spec (defaults to 0)
     * @param encoded Boolean should the string be base64 encoded (default TRUE)
     * @param hash    String hashing algorithm (defaults to SHA1)
     **/
    public communityId(int seed, Boolean encoded, byte padding, String hash) {
        this.seed = seed;
        this.encoded = encoded;
        this.hash = hash;
    }

    /**  Generate 5 Tuple CommunityId string
     * @param proto    IANA protocol name
     * @param src_ip   source ip
     * @param src_port source port
     * @param dst_ip   destination ip or null 
     * @param dst_port destination port or null
     * @return string communityId - defaults to base64 encoded string
     * @throws IllegalArgumentException  port and protocol errors
     **/
    public String getCommunityId(Protocol proto, InetAddress src_ip, int src_port, InetAddress dst_ip, Integer dst_port) {

        //verify port values
        if ((src_port | dst_port ) <0 || (src_port | dst_port) > 65535 ) {
            throw new IllegalArgumentException("ERROR: CommunityId - Invalid port or ports");
        }

        //Convert IPs to integer representation for IP comparison
        BigInteger source =  new BigInteger(1, src_ip.getAddress());
        BigInteger dest =  new BigInteger(1, dst_ip.getAddress());

        //compare IPs
        int ipSort = source.compareTo(dest);

        if (ipSort == 0) {
            ipSort = Integer.compare(src_port, dst_port);
        }

        // swap src/dst if ipSort <0, or if ip comparison is = & src < dst port 
        if (ipSort > 0) {
            InetAddress tempIp = dst_ip;
            Integer tempPort = dst_port;
            dst_ip = src_ip;
            dst_port = src_port;
            src_ip = tempIp;
            src_port = tempPort;
        }
        

        // create digest & catch exception for invalid hash value
        MessageDigest communityDigest = null;
        if (encoded) {
            try {
                communityDigest = MessageDigest.getInstance(this.hash);
            } catch (NoSuchAlgorithmException e) {
                throw new RuntimeException("ERROR: CommunityId - Invalid Msg Digest hash algorithm > " + hash , e);
            }
        }

        communityDigest.update(getBytes(seed));
        communityDigest.update(src_ip.getAddress());
        communityDigest.update(dst_ip.getAddress());
        communityDigest.update((proto.getProtocolNumber()));
        communityDigest.update(pad);
        communityDigest.update(getBytes(src_port));
        communityDigest.update(getBytes(dst_port));

        byte[] cIdBytes = communityDigest.digest();
        
        //String cId = "1:" + Base64.getEncoder().encodeToString(cIdBytes);
        //test = test + "\n sort: " + ipSort + " IP1:" + src_ip + ":" + src_port + "\t IP2" + dst_ip + ":" + dst_port + " " + proto;

        return "1:" + Base64.getEncoder().encodeToString(cIdBytes);
    }

    /**
    * get byte representation of integers
    * @param val the Integer to be converted into a two byte array.
    * @return bytes - the byte array created.
    */
    private byte[] getBytes(int val) {

        byte[] bytes = new byte[2];

        bytes[0] = (byte) (val >>> 8);
        bytes[1] = (byte) (val);

        return bytes;
    }

}

