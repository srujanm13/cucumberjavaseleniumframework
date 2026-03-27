Feature: Login

  @negative
  Scenario Outline: Invalid Login With JSON Test Data
    Given User logs in with invalid credentials from test data "<testDataKey>"
    Then User should see the invalid login error from test data

    Examples:
      | testDataKey                |
      | invalidPassword            |
      | invalidUsername            |
      | invalidUsernameAndPassword |


  @smoke
  Scenario: Valid Login
    Given User logs in with valid credentials
    Then verify login screen is displayed
