package models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Domain model representing a contact record from the {@code contacts} table.
 */
public class Contact
{
    private int contactId;
    private String firstName;
    private String middleName;
    private String lastName;
    private String nickname;
    private String phonePrimary;
    private String phoneSecondary;
    private String email;
    private String linkedinUrl;
    private LocalDate birthDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    /**
     * Default no-arg constructor.
     */
    public Contact()
    {

    }

    /**
     * Constructs a {@link Contact} from a JDBC {@link ResultSet}.
     *
     * @param rs result set positioned at a contact row
     * @throws SQLException if any column cannot be read
     */
    public Contact(ResultSet rs) throws SQLException
    {
        this.contactId = rs.getInt("contact_id");
        this.firstName = rs.getString("first_name");
        this.middleName = rs.getString("middle_name");
        this.lastName = rs.getString("last_name");
        this.nickname = rs.getString("nickname");
        this.phonePrimary = rs.getString("phone_primary");
        this.phoneSecondary = rs.getString("phone_secondary");
        this.email = rs.getString("email");
        this.linkedinUrl = rs.getString("linkedin_url");

        java.sql.Date birth = rs.getDate("birth_date");
        this.birthDate = (birth != null) ? birth.toLocalDate() : null;

        this.createdAt = rs.getTimestamp("created_at").toLocalDateTime();
        this.updatedAt = rs.getTimestamp("updated_at").toLocalDateTime();
    }

    // Getters
    public int getContactId()
    {
        return contactId;
    }
    public String getFirstName()
    {
        return firstName;
    }
    public String getMiddleName()
    {
        return middleName;
    }
    public String getLastName()
    {
        return lastName;
    }
    public String getNickname()
    {
        return nickname;
    }
    public String getPhonePrimary()
    {
        return phonePrimary;
    }
    public String getPhoneSecondary()
    {
        return phoneSecondary;
    }
    public String getEmail()
    {
        return email;
    }
    public String getLinkedinUrl()
    {
        return linkedinUrl;
    }
    public LocalDate getBirthDate()
    {
        return birthDate;
    }
    public LocalDateTime getCreatedAt()
    {
        return createdAt;
    }
    public LocalDateTime getUpdatedAt()
    {
        return updatedAt;
    }

    // Setters
    public void setContactId(int contactId)
    {
        this.contactId = contactId;
    }

    public void setFirstName(String firstName)
    {
        if (firstName != null && !firstName.isEmpty()) this.firstName = firstName;
    }

    public void setMiddleName(String middleName)
    {
        this.middleName = middleName;
    }

    public void setLastName(String lastName)
    {
        if (lastName != null && !lastName.isEmpty()) this.lastName = lastName;
    }

    public void setNickname(String nickname)
    {
        this.nickname = nickname;
    }

    public void setPhonePrimary(String phonePrimary)
    {
        if (isValidPhone(phonePrimary)) this.phonePrimary = phonePrimary;
    }

    public void setPhoneSecondary(String phoneSecondary)
    {
        if (phoneSecondary == null || isValidPhone(phoneSecondary)) this.phoneSecondary = phoneSecondary;
    }

    public void setEmail(String email)
    {
        if (isValidEmail(email)) this.email = email;
    }

    public void setLinkedinUrl(String linkedinUrl)
    {
        if (linkedinUrl == null || isValidLinkedinUrl(linkedinUrl)) this.linkedinUrl = linkedinUrl;
    }

    public void setBirthDate(LocalDate birthDate)
    {
        this.birthDate = birthDate;
    }

    public void setCreatedAt(LocalDateTime createdAt)
    {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    // Validation methods
    public boolean isValidEmail(String email)
    {
        return email != null && email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$");
    }

    public boolean isValidPhone(String phone)
    {
        return phone != null && phone.matches("\\+?[0-9]{7,15}");
    }

    public boolean isValidLinkedinUrl(String url)
    {
        return url != null && url.matches("^https?://(www\\.)?linkedin\\.com/.*$");
    }

    public String fullName()
    {
        if (middleName != null && !middleName.isEmpty())
        {
            return firstName + " " + middleName + " " + lastName;
        }
        return firstName + " " + lastName;
    }

    public boolean isAdult()
    {
        return birthDate != null && birthDate.plusYears(18).isBefore(LocalDate.now());
    }

    public void updateTimestamp()
    {
        this.updatedAt = LocalDateTime.now();
    }

    @Override
    public String toString()
    {
        return "Contact{" +
                "contactId=" + contactId +
                ", fullName='" + fullName() + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phonePrimary='" + phonePrimary + '\'' +
                ", email='" + email + '\'' +
                ", birthDate=" + birthDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
