package dip.validation.validator;

import java.net.MalformedURLException;

/**
 * Please see hibernate-validator constraint validators (JSR303 reference implementation).
 * 
 * @author nocquidant
 */
public class URLValidator extends AbstractBusinessValidator {
    private String protocol;
    private String host;
    private int port;

    public URLValidator(String violationCode) {
        super(violationCode);
    }

    public URLValidator(String violationCode, String protocol, String host, int port) {
        super(violationCode);

        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    @Override
    public boolean isValid() {
        if (getValue() == null) { // null values are valid
            return true;
        }

        if (!(getValue() instanceof String)) {
            throw new IllegalArgumentException("Value must be instance of URL: " + getValue().getClass());
        }

        String value = (String) getValue();

        if (value.length() == 0) {
            return true;
        }

        java.net.URL url;
        try {
            url = new java.net.URL(value.toString());
        } catch (MalformedURLException e) {
            return false;
        }

        if ((protocol != null) && (protocol.length() > 0) && !url.getProtocol().equals(protocol)) {
            return false;
        }

        if ((host != null) && (host.length() > 0) && !url.getHost().equals(host)) {
            return false;
        }

        if ((port != -1) && (url.getPort() != port)) {
            return false;
        }

        return true;
    }
}
