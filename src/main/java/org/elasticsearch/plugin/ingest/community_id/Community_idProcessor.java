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

import org.elasticsearch.ingest.AbstractProcessor;
import org.elasticsearch.ingest.IngestDocument;
import org.elasticsearch.ingest.Processor;

import java.net.InetAddress;
import java.io.IOException;
import java.util.Map;
import java.util.Locale;

import static org.elasticsearch.ingest.ConfigurationUtils.readStringProperty;

public class Community_idProcessor extends AbstractProcessor {

    public static final String TYPE = "community_id";

    private final String field;
    private final String targetField;

    public Community_idProcessor(String tag, String field, String targetField) throws IOException {
        super(tag);
        this.field = field;
        if (targetField == null) {
            // default return field if not specific - ecs 1.5
            this.targetField = "network.community_id";
        }
        else {
            this.targetField = targetField;
        }
    }

    @Override
    public IngestDocument execute(IngestDocument ingestDocument) throws Exception {
        String ipsPortsProtocols = ingestDocument.getFieldValue(field, String.class);
         Integer srcPort = 0;
         Integer dstPort = 0;

        //required for using string.toUpper to normalize transport protocol
        Locale ENGLISH = Locale.forLanguageTag("en"); 

        if (ipsPortsProtocols == null) {
            // exit w/o error (ignore)
            return ingestDocument;
        } 

        // parse incoming string for proto, ip, port, etc.
        String[] splits = ipsPortsProtocols.split(",");

        //strip spaces, _ & _ from port names
        //splits[0] = splits[0].replace("/[^A-Za-z0-9]/g", "");

        // create protocol from splits[0]
        Protocol proto = Protocol.valueOf(splits[0].toUpperCase(ENGLISH));

        //srcIp from splits[1&2]
        InetAddress srcIp = InetAddress.getByName(splits[1]);
        InetAddress dstIp = InetAddress.getByName(splits[2]);

        // instantiate generator
        communityId communityString = new communityId();

        // Set port for 5 tuple, or set ports to null for 3 tuple
        if (splits.length == 5) {
            //ports
            srcPort = Integer.parseInt(splits[3]);
            dstPort = Integer.parseInt(splits[4]);
        }
       
            ingestDocument.setFieldValue(targetField, communityString.getCommunityId(proto, srcIp, srcPort, dstIp, dstPort));
        
        return ingestDocument;
    }

    @Override
    public String getType() {
        return TYPE;
    }

    public static final class Factory implements Processor.Factory {

        @Override
        public Community_idProcessor create(Map<String, Processor.Factory> factories, String tag, Map<String, Object> config) 
            throws Exception {
            String field = readStringProperty(TYPE, tag, config, "field");
            String targetField = readStringProperty(TYPE, tag, config, "target_field", "default_field_name");

            return new Community_idProcessor(tag, field, targetField);
        }
    }
}
