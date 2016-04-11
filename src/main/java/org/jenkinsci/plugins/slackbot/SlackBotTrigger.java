/*
 * The MIT License
 *
 * Copyright (c) 2016 Guro Bokum
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jenkinsci.plugins.slackbot;

import java.util.logging.Logger;
import java.util.logging.Level;
import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.Cause;
import hudson.model.Descriptor;
import hudson.model.Item;
import hudson.model.ParametersAction;
import hudson.model.StringParameterValue;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;
import org.kohsuke.stapler.DataBoundConstructor;
/**
 * SlackBotTrigger {@link Trigger}.
 *
 * @author Bokum Guro
 */
public class SlackBotTrigger extends Trigger<AbstractProject> {
    private final String token;
    private static final Logger LOGGER = Logger.getLogger(SlackBotTrigger.class.getName());

    @DataBoundConstructor
    public SlackBotTrigger(String token) {
        super();
        this.token = token;
        LOGGER.log(Level.INFO, "Inited with {0} token", token);
    }

    public void onBuildRequest(StaplerRequest req, StaplerResponse res) {
        LOGGER.info("BuildRequest");
        String token = req.getParameter("token");

        if (!this.token.equals(token)) {
            LOGGER.warning("Auth failure");
            return;
        }

        String[] params = req.getParameter("text").split(" ");
        String branch = null;
        if (params.length > 1) {
            branch = params[1];
        }
        this.run(req.getRemoteAddr(), branch);
    }


    public void run(String host, String branch) {
        LOGGER.log(Level.INFO, "Run {0}", branch);
        try {
            Cause cause = new Cause.RemoteCause(host, "from slack"); 
            if (branch == null) {
                job.scheduleBuild(cause);
            } else {
                //TODO: BRANCH_SELECTOR to .jelly
                StringParameterValue value = new StringParameterValue("BRANCH_SELECTOR", branch);
                ParametersAction params = new ParametersAction(value);
                job.scheduleBuild(0, cause, params);
            }
        } catch (Exception e) {
        }
    }

    public String getToken() {
        return this.token;
    }

    @Extension
    public static final class DescriptorImpl extends TriggerDescriptor {
        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicable(Item item) {
            return item instanceof AbstractProject;
        }



        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return "Slack bot";
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getHelpFile() {
            return "/org/jenkinsci/slackbot/SlackBotTrigger/help.html";
        }
    }
}
