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

// Enum Protocol class enabled for most protocols - strip "-"s
public enum Protocol {

    HOPOPT((byte) 0),
    ICMP((byte) 1),
    IGMP((byte) 2),
    GGP((byte) 3),
    IPV4((byte) 4),
    ST((byte) 5),
    TCP((byte) 6),
    CBT((byte) 7),
    EGP((byte) 8),
    IGP((byte) 9),
    PUP((byte) 12),
    EMCON((byte) 14),
    XNET((byte) 15),
    CHAOS((byte) 16),
    UDP((byte) 17),
    MUX((byte) 18),
    HMP((byte) 20),
    PRM((byte) 21),
    RDP((byte) 27),
    IRTP((byte) 28),
    NETBLT((byte) 30),
    DCCP((byte) 33),
    IDPR((byte) 35),
    XTP((byte) 36),
    DDP((byte) 37),
    IL((byte) 40),
    IPV6((byte) 41),
    SDRP((byte) 42),
    IPV6ROUTE((byte) 43),
    IPV6FRAG((byte) 44),
    IDRP((byte) 45),
    RSVP((byte) 46),
    GRE((byte) 47),
    DSR((byte) 48),
    BNA((byte) 49),
    ESP((byte) 50),
    AH((byte) 51),
    NARP((byte) 54),
    MOBILE((byte) 55),
    TLSP((byte) 56),
    SKIP((byte) 57),
    IPV6ICMP((byte) 58),
    IPV6NONXT((byte) 59),
    IPV6OPTS((byte) 60),
    CFTP((byte) 62),
    KRYPTOLAN((byte) 65),
    RVD((byte) 66),
    IPPC((byte) 67),
    VISA((byte) 70),
    IPCV((byte) 71),
    CPNX((byte) 72),
    CPHB((byte) 73),
    WSN((byte) 74),
    PVP((byte) 75),
    VMTP((byte) 81),
    VINES((byte) 83),
    TTP((byte) 84),
    IPTM((byte) 84),
    DGP((byte) 86),
    TCF((byte) 87),
    EIGRP((byte) 88),
    OSPFIGP((byte) 89),
    LARP((byte) 91),
    MTP((byte) 92),
    IPIP((byte) 94),
    ETHERIP((byte) 97),
    ENCAP((byte) 98),
    GMTP((byte) 100),
    IFMP((byte) 101),
    PNNI((byte) 102),
    PIM((byte) 103),
    ARIS((byte) 104),
    SCPS((byte) 105),
    QNX((byte) 106),
    IPCOMP((byte) 108),
    SNP((byte) 109),
    VRRP((byte) 112),
    PGM((byte) 113),
    L2TP((byte) 115),
    DDX((byte) 116),
    IATP((byte) 117),
    STP((byte) 118),
    SRP((byte) 119),
    UTI((byte) 120),
    SMP((byte) 121),
    PTP((byte) 123),
    ISIS((byte) 124),
    FIRE((byte) 125),
    CRTP((byte) 126),
    CRUDP((byte) 127),
    SSCOPMCE((byte) 128),
    IPLT((byte) 129),
    SPS((byte) 130),
    PIPE((byte) 131),
    SCTP((byte) 132),
    FC((byte) 133),
    UDPLITE((byte) 136),
    MANET((byte) 138),
    HIP((byte) 139),
    SHIM6((byte) 140),
    WESP((byte) 141),
    ROHC((byte) 142),
    ETHERNET((byte) 143);

    private final byte protocolNumber;

    Protocol(byte protocolNumber) {
        this.protocolNumber = protocolNumber;
    }

    public byte getProtocolNumber() {
        return protocolNumber;
    }
}

