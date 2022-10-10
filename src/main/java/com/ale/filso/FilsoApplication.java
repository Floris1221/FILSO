package com.ale.filso;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@Theme(value = "resources")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@Push
@EnableCaching
public class FilsoApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(FilsoApplication.class, args);
	}

}
