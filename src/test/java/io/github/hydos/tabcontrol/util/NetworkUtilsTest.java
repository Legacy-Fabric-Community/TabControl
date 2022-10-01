package io.github.hydos.tabcontrol.util;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class NetworkUtilsTest {

	@BeforeEach
	void setUp() {
		NetworkUtils.SERVER_PLACEHOLDERS.put("bruh", () -> "bruh");
	}

	@Test
	void format() {
		Assertions.assertEquals(NetworkUtils.format("lol it is ${server.bruh} lmfao"), "lol it is bruh lmfao");
		Assertions.assertEquals(NetworkUtils.format("lol it is ${server.kekw} lmfao"), "lol it is %INVALIDTOKEN% lmfao");
	}
}
