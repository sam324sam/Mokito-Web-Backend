package com.mokito.backend;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.concurrent.atomic.AtomicReference;

import javax.swing.SwingUtilities;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.mokito.backend.gui.ServerGui;
import com.mokito.backend.gui.helpers.PlayerListener;
import com.mokito.backend.service.WebSocketSessionManager;

@SpringBootApplication
public class MokitoWebBackendApplication {

	private static final Logger logger = LoggerFactory.getLogger(MokitoWebBackendApplication.class);
	private static final AtomicReference<ServerGui> GUI_REF = new AtomicReference<>();

	public static ServerGui getGui() {
		return GUI_REF.get();
	}

	public static void main(String[] args) {
		try {
			SwingUtilities.invokeAndWait(() -> GUI_REF.set(new ServerGui()));
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			logger.error("Hilo interrumpido al iniciar el GUI", e);
			return;
		} catch (Exception e) {
			logger.error("Error al iniciar el GUI", e);
			return;
		}

		redirectToLogger();

		Thread springThread = new Thread(() -> {
			try {
				var context = SpringApplication.run(MokitoWebBackendApplication.class, args);
				WebSocketSessionManager sessionManager = context.getBean(WebSocketSessionManager.class);

				ServerGui gui = GUI_REF.get();

				sessionManager.setPlayerListener(new PlayerListener() {
					@Override
					public void onPlayerConnected(String name) {
						gui.addPlayer(name);
					}

					@Override
					public void onPlayerDisconnected(String name) {
						gui.removePlayer(name);
					}
				});

				SwingUtilities.invokeLater(() -> {
					gui.log("✅ Servidor iniciado correctamente");
					gui.setStatus(true);
				});

			} catch (Exception e) {
				logger.error("Error al iniciar Spring", e);
				SwingUtilities.invokeLater(() -> {
					GUI_REF.get().log("❌ Error al iniciar: " + e.getMessage());
					GUI_REF.get().setStatus(false);
				});
			}
		}, "spring-main");

		springThread.setDaemon(false);
		springThread.start();
	}

	private static void redirectToLogger() {
		PrintStream loggerStream = new PrintStream(new OutputStream() {
			private final StringBuilder buffer = new StringBuilder();

			@Override
			public void write(int b) {
				char c = (char) b;
				if (c == '\n') {
					String line = buffer.toString().trim();
					if (!line.isEmpty()) {
						logger.info(line);
						ServerGui gui = GUI_REF.get();
						if (gui != null) {
							SwingUtilities.invokeLater(() -> gui.log(line));
						}
					}
					buffer.setLength(0);
				} else {
					buffer.append(c);
				}
			}
		}, true);

		System.setOut(loggerStream);
		System.setErr(loggerStream);
	}
}