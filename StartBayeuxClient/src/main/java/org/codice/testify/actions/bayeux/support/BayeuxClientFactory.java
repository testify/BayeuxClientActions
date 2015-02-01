/*
 * Copyright 2015 Codice Foundation
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 */

package org.codice.testify.actions.bayeux.support;

import org.codice.testify.objects.TestifyLogger;
import org.cometd.bayeux.Channel;
import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;
import org.cometd.client.transport.LongPollingTransport;
import org.eclipse.jetty.client.HttpClient;

public class BayeuxClientFactory {

    public BayeuxClient spawnClient(String url) throws Exception {

        HttpClient httpClient = new HttpClient();
        httpClient.start();

        // Prepare the transport using default options
        LongPollingTransport transport = new LongPollingTransport(null, httpClient);

        // Create the BayeuxClient
        TestifyLogger.debug( "CometD Endpoint is: " + url, this.getClass().getSimpleName());

        BayeuxClient client = new BayeuxClient(url, transport);

        // Register the message listener for the handshake
        client.getChannel(Channel.META_HANDSHAKE).addListener(new ClientSessionChannel.MessageListener()
        {
            public void onMessage(ClientSessionChannel channel, Message message)
            {
                TestifyLogger.info("Meta Channel Handshake: " + message, this.getClass().getSimpleName());
                TestifyLogger.info("Session ID: " + channel.getSession().getId(), this.getClass().getSimpleName());

                boolean success = message.isSuccessful();
                if (!success) {
                    String error = (String) message.get("error");
                    if (error != null) {
                        TestifyLogger.error("Error during HANDSHAKE: " + error, this.getClass().getSimpleName());
                    }
                }
            }
        });

        // Initiate the handshake
        client.handshake();
        TestifyLogger.info("Waiting for Handshake (10 seconds)", this.getClass().getSimpleName());
        client.waitFor(10 * 1000, BayeuxClient.State.CONNECTED);

        return client;
    }
}