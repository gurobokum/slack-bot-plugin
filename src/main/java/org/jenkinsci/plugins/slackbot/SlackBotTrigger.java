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

import hudson.Extension;
import hudson.model.Item;
import hudson.model.Job;
import hudson.triggers.Trigger;
import hudson.triggers.TriggerDescriptor;
import hudson.tasks.BuildTrigger;

import org.kohsuke.stapler.DataBoundConstructor;
/**
 * SlackBotTrigger {@link Trigger}.
 *
 * @author Bokum Guro
 */
public class SlackBotTrigger extends Trigger<Job> {
    private final String token;

    @DataBoundConstructor
    public SlackBotTrigger (String token) {
        this.token = token;
    }

    @Extension
    public static class DescriptorIml extends TriggerDescriptor {
        public DescriptorIml() {
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean isApplicable(Item item) {
            return item instanceof Job;
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public String getDisplayName() {
            return "Slack bot";
        }
    }
}
