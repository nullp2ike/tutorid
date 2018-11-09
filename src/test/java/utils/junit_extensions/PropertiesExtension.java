package utils.junit_extensions;

import org.junit.jupiter.api.extension.BeforeAllCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import utils.Setup;

import java.io.FileInputStream;
import java.util.Properties;

public class PropertiesExtension implements BeforeAllCallback {

    @Override
    public void beforeAll(ExtensionContext context) throws Exception {
        Properties prop = new Properties();
        prop.load(new FileInputStream("environment.properties"));
        new Setup(prop);
    }

}
