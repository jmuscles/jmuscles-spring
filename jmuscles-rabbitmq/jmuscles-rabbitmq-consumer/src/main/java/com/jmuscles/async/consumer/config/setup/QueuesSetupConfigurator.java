/**
 * 
 */
package com.jmuscles.async.consumer.config.setup;

import java.util.Map;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.util.StringUtils;

import com.jmuscles.async.consumer.util.QueueTypes;
import com.jmuscles.async.consumer.util.RabbitmqSpringBeanUtil;

/**
 * @author manish goel
 *
 */
public class QueuesSetupConfigurator {

	public static void configure(RabbitmqSetupConfigurator configurator) {
		setupQueues(configurator);
	}

	private static void setupQueues(RabbitmqSetupConfigurator configurator) {
		configurator.getRabbitmqConfig().getQueueSetsConfig().values().forEach(q -> {
			String exchange = q.getExchange();
			String primaryQueueName = q.getName();
			String abandonedQueueName = QueueTypes.queueName(primaryQueueName, QueueTypes.ABANDONED);
			configureQueue(abandonedQueueName, exchange, null, q.getArguments().get(QueueTypes.ABANDONED),
					configurator);
			if (!q.isRetrySetupDisabled()) {
				String retryQueueName = QueueTypes.queueName(primaryQueueName, QueueTypes.RETRY);
				configureQueue(retryQueueName, exchange, abandonedQueueName, q.getArguments().get(QueueTypes.RETRY),
						configurator);
				configureQueue(QueueTypes.queueName(primaryQueueName, QueueTypes.WAIT), exchange, retryQueueName,
						q.getArguments().get(QueueTypes.WAIT), configurator);
			}
			configureQueue(primaryQueueName, exchange, abandonedQueueName, q.getArguments().get(QueueTypes.PRIMARY),
					configurator);
		});

	}

	private static void configureQueue(String queueName, String exchangeName, String errorRoutingKey,
			Map<String, Object> arguments, RabbitmqSetupConfigurator configurator) {
		configureQueue(queueName, exchangeName, errorRoutingKey, exchangeName, arguments, configurator);
	}

	private static void configureQueue(String queueName, String exchangeName, String errorRoutingKey,
			String errorExchange, Map<String, Object> arguments, RabbitmqSetupConfigurator configurator) {

		QueueBuilder queueBuilder = QueueBuilder.durable(queueName);
		if (arguments != null) {
			queueBuilder = queueBuilder.withArguments(arguments);
		}
		if (StringUtils.hasText(errorRoutingKey)) {
			queueBuilder.deadLetterRoutingKey(errorRoutingKey);
			queueBuilder.deadLetterExchange(errorExchange);
		}
		Queue queue = queueBuilder.build();
		RabbitmqSpringBeanUtil.registerRabbitEntityAsSpringBean(queueName, Queue.class, queue,
				configurator.getBeanFactory());
		Exchange exchange = StringUtils.hasText(exchangeName)
				? (Exchange) RabbitmqSpringBeanUtil.getRabbitEntitySpringBean(exchangeName, Exchange.class,
						configurator.getBeanFactory())
				: null;
		if (exchange != null) {
			RabbitmqSpringBeanUtil.registerRabbitEntityAsSpringBean(queueName, Binding.class,
					BindingBuilder.bind(queue).to(exchange).with(queueName).noargs(), configurator.getBeanFactory());
		}

	}

}
