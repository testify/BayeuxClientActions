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

package org.codice.testify.actions.bayeux;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

import org.codice.testify.actions.Action;
import org.codice.testify.objects.AllObjects;
import org.codice.testify.objects.TestifyLogger;
import org.codice.testify.objects.Request;
import org.codice.testify.objects.TestProperties;

import org.cometd.bayeux.Message;
import org.cometd.bayeux.client.ClientSessionChannel;
import org.cometd.client.BayeuxClient;


/**
 * The AddBayeuxSubscription class is a Testify Action service for creating subscriptions for a client using the Bayeux protocol
 * @see org.codice.testify.actions.Action
 */
public class AddBayeuxSubscription implements BundleActivator, Action {

    @Override
    public void executeAction(String s) {

        TestifyLogger.debug("Running AddBayeuxSubscription Action", this.getClass().getSimpleName());

        // Grab the test properties
        final TestProperties testProperties = (TestProperties) AllObjects.getObject("testProperties");

        final Request request = (Request) AllObjects.getObject("request");

        if (s != null) {
            // Split action info by "=="
            TestifyLogger.debug(s, this.getClass().getSimpleName());
            String[] actionElements = s.split("==");

            // If there are not two action elements, then produce an error
            if (actionElements.length == 2) {

                // Split the two action elements into separate strings
                final String clientName = actionElements[0].trim();
                final String channel = "/"+actionElements[1].trim();

                // Check that the client name and channel are greater than one character
                if (clientName.length() > 0 && channel.length() > 0) {

                    // Retrieve the BayeuxClient instance from AllObjects
                    final BayeuxClient client = (BayeuxClient)AllObjects.getObject(clientName);

                    // Create a response object for the client instance
                    final StringBuilder jsonResponse = (StringBuilder)AllObjects.getObject(clientName + ".response");

                    // Add subscription to client
                    try {
                        client.getChannel(channel).subscribe(new ClientSessionChannel.MessageListener() {
                            @Override
                            public void onMessage(ClientSessionChannel clientSessionChannel, Message message) {
                                String json = message.getJSON();
                                jsonResponse.append(json);
                            }
                        });
                    } catch(java.lang.IllegalArgumentException e) {
                        TestifyLogger.error("Unable to subscribe to "+ channel +". "+"Caught java.lang.IllegalArgumentException: " + e, this.getClass().getSimpleName());
                    } catch(java.lang.NullPointerException e) {
                        TestifyLogger.error("Unable to subscribe to "+ channel +". "+"Caught java.lang.NullPointerException: " + e, this.getClass().getSimpleName());
                    }
                }
                else { TestifyLogger.error("The two elements contained in action info are zero characters in length", this.getClass().getSimpleName()); }
            }
            else { TestifyLogger.error("Provided Action info: " + s + " is invalid, requires two elements but found: " + actionElements.length, this.getClass().getSimpleName()); }
        }
        else { TestifyLogger.error("Action info must be provided for this action", this.getClass().getSimpleName()); }
    }

    @Override
    public void start(BundleContext bundleContext) throws Exception {

        // Register the AddBayeuxSubscription service
        bundleContext.registerService(Action.class.getName(), new AddBayeuxSubscription(), null);
    }

    @Override
    public void stop(BundleContext bundleContext) throws Exception {

    }
}
