# Elasticsearch community_id Ingest Processor

This ingest processor calculates community_id from a comma seperated
string consisting of:
 - 5 Tuple Network Fields
   - network.transport, source.ip, destination.ip, source.port, destination.port
 - 3 Tuple Network Fields
  - network.transport, source.ip, destination.ip,

Error checking and exception handling is currently marginal, most likely to result in an ignore situaiton.

The calculation is based on the Zeek community id spec
 - https://github.com/corelight/zeek-community-id

With thanks to Rapid7 for their java implementation which I used for  "I am not really a java programmer" fixes (byte representation of the padding, ports, & protocols, and using enums, specifically)

## Usage
 - Community ID Pipeline (5 Tuple)
```PUT  /_ingest/pipeline/community_id
{
   "description" : "Community_Id Generator",
    "processors" : [
      {
        "set" : {
          "field" : "temp.communityString",
          "value" : "{{network.transport}},{{source.ip}},{{destination.ip}},{{source.port}},{{destination.port}}"
        }
      },
      {
        "community_id" : {
          "field" : "temp.communityString",
          "target_field" : "network.community_id"
        }
      },
      {
        "remove" : {
          "field" : "temp.communityString"
        }
      }
    ]
  }
 ```
  - Call to community_id pipeline
```  {
        "pipeline" : {
           "if" : "ctx.network?.community_id == null && ctx.source?.ip != null && ctx.source?.port != null && ctx.destination?.ip != null && ctx.destination?.port != null && ctx.network?.transport != null",
           "name" : "community_id"
        }
      }
```

## Configuration

Currently only target field, all other options are set for defaults (0 byte pad, 0 seed, "1:" version prefix, and base64 encoding)

target field defaults to network.community_id, but will accept another field if provided

## Setup

The repo has latest distribution in the plugin directory, the compatibility has been set for 7.8 (have not figured out how to compile for e.g. 7.*)

```bash
gradle clean check
```

This will produce a zip file in `build/distributions`.

After building the zip file, you can install it like this

```bash
bin/elasticsearch-plugin install file:///path/to/ingest-community_id/build/distribution/ingest-community_id-0.0.1-SNAPSHOT.zip
```

## Bugs & TODO

* Change input to multiple fields if possible (no temp string)
      - or just pull the 5 tuple/3 tuple fields based on 1 input (e.g. tuple = 3||5
* Implement config options


