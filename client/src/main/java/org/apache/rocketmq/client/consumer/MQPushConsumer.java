/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.rocketmq.client.consumer;

import org.apache.rocketmq.client.consumer.listener.MessageListener;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQClientException;

/**
 * Push consumer
 */

/**
 * 名字虽然是 Push 开头，实际在实现时，使用 Pull 方式实现。通过 Pull 不断不断不断轮询 Broker 获取消息。
 * 当不存在新消息时，Broker 会挂起请求，直到有新消息产生，取消挂起，返回新消息。这样，基本和 Broker 主动
 * Push 做到接近的实时性（当然，还是有相应的实时性损失）。原理类似 长轮询( Long-Polling )。
 */
public interface MQPushConsumer extends MQConsumer {
    /**
     * Start the consumer
     */
    void start() throws MQClientException;

    /**
     * Shutdown the consumer
     */
    void shutdown();

    /**
     * Register the message listener
     */
    @Deprecated
    void registerMessageListener(MessageListener messageListener);

    /**
     * 注册并发消息事件监昕器
     *
     * @param messageListener
     */
    void registerMessageListener(final MessageListenerConcurrently messageListener);

    /**
     * 注册顺序消费事件监听器
     *
     * @param messageListener
     */
    void registerMessageListener(final MessageListenerOrderly messageListener);

    /**
     * Subscribe some topic
     *
     * @param subExpression subscription expression.it only support or operation such as "tag1 || tag2 || tag3" <br> if
     * null or * expression,meaning subscribe
     * all
     */
    /**
     * 基于主题订阅消息
     *
     * @param topic         消息主题
     * @param subExpression 消息过滤表达式， TAG 或 SQL92 表达式
     * @throws MQClientException
     */
    void subscribe(final String topic, final String subExpression) throws MQClientException;

    /**
     * This method will be removed in the version 5.0.0,because filterServer was removed,and method <code>subscribe(final String topic, final MessageSelector messageSelector)</code>
     * is recommended.
     *
     * Subscribe some topic
     *
     * @param fullClassName full class name,must extend org.apache.rocketmq.common.filter. MessageFilter
     * @param filterClassSource class source code,used UTF-8 file encoding,must be responsible for your code safety
     */
    /**
     * 基于主题订阅消息，消息过滤方式使用类模式
     *
     * @param topic             消息主题
     * @param fullClassName     过滤类全路径名
     * @param filterClassSource 过滤类代码
     * @throws MQClientException
     */
    @Deprecated
    void subscribe(final String topic, final String fullClassName,
        final String filterClassSource) throws MQClientException;

    /**
     * Subscribe some topic with selector.
     * <p>
     * This interface also has the ability of {@link #subscribe(String, String)},
     * and, support other message selection, such as {@link org.apache.rocketmq.common.filter.ExpressionType#SQL92}.
     * </p>
     * <p/>
     * <p>
     * Choose Tag: {@link MessageSelector#byTag(java.lang.String)}
     * </p>
     * <p/>
     * <p>
     * Choose SQL92: {@link MessageSelector#bySql(java.lang.String)}
     * </p>
     *
     * @param selector message selector({@link MessageSelector}), can be null.
     */
    void subscribe(final String topic, final MessageSelector selector) throws MQClientException;

    /**
     * Unsubscribe consumption some topic
     *
     * @param topic message topic
     */
    /**
     * 取消消息订阅
     */
    void unsubscribe(final String topic);

    /**
     * Update the consumer thread pool size Dynamically
     */
    void updateCorePoolSize(int corePoolSize);

    /**
     * Suspend the consumption
     */
    void suspend();

    /**
     * Resume the consumption
     */
    void resume();
}
