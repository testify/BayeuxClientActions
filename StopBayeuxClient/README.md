StopBayeuxClient Action
========================
*Testify Action for stopping bayeux client instances*

# Usage
### StopBayeuxClient
  *Takes in a name which is used as a unique identifier for the client instance to destroy*

    StopBayeuxClient::{client-name}

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

