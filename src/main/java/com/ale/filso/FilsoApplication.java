package com.ale.filso;

import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.theme.Theme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Theme(value = "resources")
@NpmPackage(value = "line-awesome", version = "1.3.0")
@Push
public class FilsoApplication implements AppShellConfigurator {

	public static void main(String[] args) {
		SpringApplication.run(FilsoApplication.class, args);
	}

}
