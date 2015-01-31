AddBayeuxSubscription Action
=========================
*Testify Action for adding subscriptions to a bayeux client*

# Usage
### AddBayeuxSubscription
  *Takes in a name which is used as a unique identifier for the client to add the subscription to as well as a channel to subscribe to*

    StartBayeuxClientSubscription::{client-name}=={channel}
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