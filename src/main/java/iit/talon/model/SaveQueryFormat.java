package iit.talon.model;

/**
 * SaveQueryFormat
 */
public class SaveQueryFormat {
    private String clientId;
    private String key;
    private String value;
    /**
     * @return the clientId
     */
    public String getClientId() {
        return clientId;
    }
    /**
     * @return the key
     */
    public String getKey() {
        return key;
    }
    /**
     * @return the value
     */
    public String getValue() {
        return value;
    }
    /**
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @param value the value to set
     */
    public void setValue(String value) {
        this.value = value;
    }
    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}