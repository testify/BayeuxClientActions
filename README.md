Testify Bayeux Client Actions
=============================
*Bayeux Client Actions for Testify*

These actions provide the ability to spin up/destroy a client for a given endpoint using the Bayeux protocol and manage the clients subscriptions

# Available Actions
* StartBayeuxClient
* StopBayeuxClient
* AddBayeuxSubscription

# Usage
### StartBayeuxClient
  *Takes in a name which is used as a unique identifier for a client instance and an endpoint to connect to as its parameters*

    StartBayeuxClient::{client-name}=={client-endpoint}

### StopBayeuxClient
  *Takes in a name which is used as a unique identifier for the client instance to destroy*

    StopBayeuxClient::{client-name}

### AddBayeuxSubscription
  *Takes in a name which is used as a unique identifier for the client to add the subscription to as well as a channel to subscribe to*

    AddBayeuxClientSubscription::{client-name}=={channel}
## Example

    <testcase>
      ..
      ..
      <preTestProcessorAction>
        StartBayeuxClient::{client-name}=={client-endpoint}
        AddBayeuxSubscription::{client-name}==${channel}
      </preTestProcessorAction>
      ..
      ..
      <postTestProcessorAction>
        StopBayeuxClient::{client-name}
      </postTestProcessorAction>
      ..
    </testcase>

