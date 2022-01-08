/**
 * 
 */
package com.jmuscles.async.consumer.config.setup;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.util.StringUtils;

import com.jmuscles.async.consumer.util.RabbitmqSpringBeanUtil;

/**
 * @author manish goel
 *
 */
public class ExchangesSetupConfigurator {

	public static void configure(RabbitmqSetupConfigurator configurator) {
		setupExchanges(configurator);
	}

	private static void setupExchanges(RabbitmqSetupConfigurator configurator) {
		configurator.getRabbitmqConfig().getExchanges().forEach(e -> {
			Exchange exchange = (new ExchangeBuilder(e.getName(), e.getType())).durable(true).build();
			RabbitmqSpringBeanUtil.registerRabbitEntityAsSpringBean(e.getName(), Exchange.class, exchange,
					configurator.getBeanFactory());

			if (StringUtils.hasText(e.getParent())) {
				Exchange parentExchange = (Exchange) RabbitmqSpringBeanUtil.getRabbitEntitySpringBean(e.getParent(),
						Exchange.class, configurator.getBeanFactory());
				RabbitmqSpringBeanUtil.registerRabbitEntityAsSpringBean(e.getName(), Binding.class,
						BindingBuilder.bind(exchange).to(parentExchange).with("").noargs(),
						configurator.getBeanFactory());
			}
		});
	}

}
