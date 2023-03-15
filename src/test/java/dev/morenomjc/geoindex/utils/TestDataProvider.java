package dev.morenomjc.geoindex.utils;

import java.io.File;
import java.nio.charset.Charset;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.testcontainers.shaded.org.apache.commons.io.FileUtils;

@NoArgsConstructor
public class TestDataProvider {

	@SneakyThrows
	public static String readStringFromFile(String fileName) {
		return FileUtils.readFileToString(new File(fileName), Charset.defaultCharset());
	}

}
