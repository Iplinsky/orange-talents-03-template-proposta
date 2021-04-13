package br.com.zupacademy.proposta.health.check;

import java.sql.Connection;
import java.sql.Statement;

import javax.sql.DataSource;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class HealthCheck implements HealthIndicator {

	public DataSource ds;

	public HealthCheck(DataSource ds) {
		super();
		this.ds = ds;
	}

	@Override
	public Health health() {
		try (Connection conn = ds.getConnection()) {
			Statement statement = conn.createStatement();
			statement.execute("SELECT TOP 1 FROM PROPOSTA");
		} catch (Exception e) {
			return Health.outOfService().withException(e).build();
		}
		return Health.up().build();
	}

}
