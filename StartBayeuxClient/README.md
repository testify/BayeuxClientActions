StartBayeuxClient Action
========================
*Testify Action for starting bayeux client instances*

# Usage
### StartBayeuxClient
  *Takes in a name which is used as a unique identifier for a client instance and an endpoint to connect to as its parameters*

    StartBayeuxClient::{client-name}=={client-endpoint}

## Example

    <testcase>
      ..
      ..
      <preTestProcessorAction>
        StartBayeuxClient::{client-name}=={client-endpoint}
        StartBayeuxSubscription::{client-name}==${channel}
      </preTestProcessorAction>
      ..
      ..
      <postTestProcessorAction>
        StopBayeuxClient::{client-name}
      </postTestProcessorAction>
      ..
    </testcase>