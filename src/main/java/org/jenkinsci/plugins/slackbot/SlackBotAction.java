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
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import jenkins.model.Jenkins;

import hudson.Extension;
import hudson.model.AbstractProject;
import hudson.model.UnprotectedRootAction;
import hudson.triggers.Trigger;

import org.kohsuke.stapler.StaplerRequest;
import org.kohsuke.stapler.StaplerResponse;

/**
 * SlackBotAction
 *
 * @author Bokum Guro
 */
@Extension
public class SlackBotAction implements UnprotectedRootAction {
    private static final String WEBHOOK_URL = "slack-bot";
    private static final Logger LOGGER = Logger.getLogger(SlackBotAction.class.getName());

    /**
     * {@inheritDoc}
     */
    @Override
    public String getUrlName() {
        return WEBHOOK_URL;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getDisplayName() {
        return null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getIconFileName() {
        return null;
    }

    public void doBuild(StaplerRequest req, StaplerResponse res) {
        LOGGER.info("Build");
        String text = req.getParameter("text");
        if (text == null) {
            return;
        }
        String[] params = text.split(" ");
        String jobName = params[0];

        AbstractProject job = getProjectByName(jobName);
        if (job == null) {
            LOGGER.log(Level.WARNING, "No job with name {0}", jobName);
            return;
        }

        SlackBotTrigger trigger = (SlackBotTrigger)job.getTrigger(SlackBotTrigger.class);
        if (trigger == null) {
            LOGGER.warning("Slack bot trigger is not enabled");
            return;
        }

        trigger.onBuildRequest(req, res);
    }

    private AbstractProject<?,?> getProjectByName(String name) {
        for (AbstractProject<?,?> job: Jenkins.getInstance().getAllItems(AbstractProject.class)) {
            if (name.equals(job.getName()) ) {
                return job;
            }
        }
        return null;
    }
}
