# CMPE343-OOP-Project2-Group7

*Developing a console application in Java using object-oriented paradigms and database integration*

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

**START OF PROGRAM**

*The application will run on the console (colorful) and will display ASCII animations at startup and shutdown.*

The application begins with a login screen (users must authenticate themselves to access the system)
- [1] Upon successful login, the system identifies the user's role and displays the corresponding menu. **Note that your application will display the user’s name and surname** at the top of the menu(s)
- [2] If the credentials are incorrect, the user will be prompted to retry.

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

**TABLES CREATED WITH MySQL**

*USERS TABLE FIELDS*
- user_id
- username
- password_hash
- first_name
- last_name
- user_role
- created_at
- (and more similar)

*CONTACTS TABLE FIELDS*
- contact_id
- first_name
- middle_name (opt.)
- last_name
- nickname
- phone_primary
- phone_secondary (opt.)
- email
- linkedin_url (opt.)
- birth_date
- created_at
- updated_at
- (and more similar)

**Note: Everyone will use the following to enter MySQL**
- USERNAME:myuser@localhost 
- PASSWORD: 1234

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

**OPERATIONS OF EACH ROLE**

Operations in **bold** are unique to that user.

*Tester* 
- change password
- logout
- list all contacts
- search by selected field(s)
- sort results in ascending or descending order by any selected (by user) field
  
*Junior Developer*
- change password
- logout
- list all contacts
- search by selected field(s)
- sort results in ascending or descending order by any selected (by user) field
- **update existing contact**
  
*Senior Developer* 
- change password
- logout
- list all contacts
- search by selected field(s)
- sort results in ascending or descending order by any selected (by user) field
- update existing contact
- **add new contact(s)**
- **delete existing contact(s)**

*Manager*
- change password
- logout
- **contacts statistical info**
- **list all users**
- **update existing user**
- **add/employ new user**
- **delete/fire existing user**

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

**OPERATION IMPLEMENTATION**

The system must support THREE **DISTINCT** “search by selected field” operations + THREE “search by selected fields” operations

*EXAMPLES for single-field search:* - ("Basic" operations)
1) Search by First Name (e.g. "Emir")
2) Search by Last Name (e.g. "Kumbasar")
3) Search by Phone Number (e.g "05418003556")

*EXAMPLES for multi-field search:*
1) Search by First Name and Birth Date (e.g. "Jana" AND "2005-09-12")
2) Search by Phone Number and Email Address (e.g. phone number contains "55" AND email address contains "yah")

**The system must be flexible, composable search criteria capable of supporting both exact and partial (substring-based) matches across multiple fields**

------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------

**DESIGNS OPEN FOR IMPLEMENTAION**

- Applying the core principles of object-oriented programming: **inheritance, polymorphism, encapsulation, and abstraction**

- Validating contact records (examples:)
- 1) Enforcing proper phone number formats
  2) Rejecting logically invalid dates (e.g., February 30)
  3) Ensuring the overall consistency and correctness of user-provided data

- What types of statistical information (contacts statistical info) can managers derive from the contact table? (examples:)
- 1) Number of individuals sharing the same first or last name
  2) Number of contacts with or without a LinkedIn URL
  3) Identification of the youngest and oldest contacts
  4) Average age of all contacts

- How the system will support **Undo operations** (following update, add, or delete actions, including the underlying mechanism)
