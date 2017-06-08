package Business;

        import com.fasterxml.jackson.annotation.JsonAnyGetter;
        import com.fasterxml.jackson.annotation.JsonAnySetter;
        import com.fasterxml.jackson.annotation.JsonIgnore;
        import com.fasterxml.jackson.annotation.JsonInclude;
        import com.fasterxml.jackson.annotation.JsonProperty;
        import com.fasterxml.jackson.annotation.JsonPropertyOrder;

        import java.util.HashMap;
        import java.util.Map;


@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id",
        "givenName",
        "surName",
        "initials",
        "displayName",
        "department",
        "personalTitle",
        "photo",
        "present"
})
public class Person {

    @JsonProperty("id")
    private String id;
    @JsonProperty("givenName")
    private String givenName;
    @JsonProperty("surName")
    private String surName;
    @JsonProperty("initials")
    private String initials;
    @JsonProperty("displayName")
    private String displayName;
    @JsonProperty("department")
    private String department;
    @JsonProperty("personalTitle")
    private Object personalTitle;
    @JsonProperty("photo")
    private String photo;
    @JsonProperty("present")
    private Boolean present;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public Person() {
    }

    /**
     *
     * @param id
     * @param present
     * @param initials
     * @param department
     * @param surName
     * @param givenName
     * @param displayName
     * @param photo
     * @param personalTitle
     */
    public Person(String id, String givenName, String surName, String initials, String displayName, String department, Object personalTitle, String photo, Boolean present) {
        super();
        this.id = id;
        this.givenName = givenName;
        this.surName = surName;
        this.initials = initials;
        this.displayName = displayName;
        this.department = department;
        this.personalTitle = personalTitle;
        this.photo = photo;
        this.present = present;
    }

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    public Person withId(String id) {
        this.id = id;
        return this;
    }

    @JsonProperty("givenName")
    public String getGivenName() {
        return givenName;
    }

    @JsonProperty("givenName")
    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

    public Person withGivenName(String givenName) {
        this.givenName = givenName;
        return this;
    }

    @JsonProperty("surName")
    public String getSurName() {
        return surName;
    }

    @JsonProperty("surName")
    public void setSurName(String surName) {
        this.surName = surName;
    }

    public Person withSurName(String surName) {
        this.surName = surName;
        return this;
    }

    @JsonProperty("initials")
    public String getInitials() {
        return initials;
    }

    @JsonProperty("initials")
    public void setInitials(String initials) {
        this.initials = initials;
    }

    public Person withInitials(String initials) {
        this.initials = initials;
        return this;
    }

    @JsonProperty("displayName")
    public String getDisplayName() {
        return displayName;
    }

    @JsonProperty("displayName")
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Person withDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    @JsonProperty("department")
    public String getDepartment() {
        return department;
    }

    @JsonProperty("department")
    public void setDepartment(String department) {
        this.department = department;
    }

    public Person withDepartment(String department) {
        this.department = department;
        return this;
    }

    @JsonProperty("personalTitle")
    public Object getPersonalTitle() {
        return personalTitle;
    }

    @JsonProperty("personalTitle")
    public void setPersonalTitle(Object personalTitle) {
        this.personalTitle = personalTitle;
    }

    public Person withPersonalTitle(Object personalTitle) {
        this.personalTitle = personalTitle;
        return this;
    }

    @JsonProperty("photo")
    public String getPhoto() {
        return photo;
    }

    @JsonProperty("photo")
    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public Person withPhoto(String photo) {
        this.photo = photo;
        return this;
    }

    @JsonProperty("present")
    public Boolean getPresent() {
        return present;
    }

    @JsonProperty("present")
    public void setPresent(Boolean present) {
        this.present = present;
    }

    public Person withPresent(Boolean present) {
        this.present = present;
        return this;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

    public Person withAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
        return this;
    }

}