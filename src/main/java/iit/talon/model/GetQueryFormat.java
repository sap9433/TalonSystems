package iit.talon.model;

/**
 * SaveQueryFormat
 */
public class GetQueryFormat {
    private String clientId;
    private String key;
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
     * @param key the key to set
     */
    public void setKey(String key) {
        this.key = key;
    }
    /**
     * @param clientId the clientId to set
     */
    public void setClientId(String clientId) {
        this.clientId = clientId;
    }
}