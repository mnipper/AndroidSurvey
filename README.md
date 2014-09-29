AndroidSurvey
=============
**Master**
![Build status](https://travis-ci.org/mnipper/AndroidSurvey.png?branch=master)

**Develop**
![Build status](https://travis-ci.org/mnipper/AndroidSurvey.png?branch=develop)

A survey application for Android with a [rails backend](https://github.com/mnipper/rails_survey).  The Android app syncs instruments and survey results automatically when internet is detected on the Android device.  This application is being designed for conducting research-oriented surveys.  This application is being developed at Duke University in a joint effort between adaptlab and CHPIR.

## Documentation
* [Model Summary](https://github.com/mnipper/AndroidSurvey/wiki/Model-summary)
* [Design Overview](https://github.com/mnipper/AndroidSurvey/wiki/Design-Overview)
* [Application Initialization](https://github.com/mnipper/AndroidSurvey/wiki/Application-Initialization)
* [Requiring Encryption](https://github.com/mnipper/AndroidSurvey/wiki/Requiring-Encryption)
* [Adding a Question Type](https://github.com/mnipper/AndroidSurvey/wiki/Adding-a-Question-Type)
* [Syncing with a Remote Table](https://github.com/mnipper/AndroidSurvey/wiki/Syncing-with-Remote-Tables)
* [Checking for Network Errors](https://github.com/mnipper/AndroidSurvey/wiki/Checking-For-Network-Errors)
* [Adding a Language](https://github.com/mnipper/AndroidSurvey/wiki/Adding-a-Language)
* [Admin Settings](https://github.com/mnipper/AndroidSurvey/wiki/Admin-Settings)
* [Translation Models](https://github.com/mnipper/AndroidSurvey/wiki/Translation-Models)
* [Following Up Questions](https://github.com/mnipper/AndroidSurvey/wiki/Following-Up-Questions)
* [Response Validations](https://github.com/mnipper/AndroidSurvey/wiki/Response-Validations)
* [Seeding the Database](https://github.com/mnipper/AndroidSurvey/wiki/Seeding-the-Database)
* [Unit Tests](https://github.com/mnipper/AndroidSurvey/wiki/Unit-Tests)
* [Continuous Integration](https://github.com/mnipper/AndroidSurvey/wiki/Continuous-Integration)
* [Launching a Survey from Another Application](https://github.com/mnipper/AndroidSurvey/wiki/Launching-a-Survey-from-another-Application)
* [API Keys and Admin Password](https://github.com/mnipper/AndroidSurvey/wiki/API-Keys-and-Admin-Password)
* [Creating Another App](https://github.com/mnipper/AndroidSurvey/wiki/Creating-Another-App)
* [Obtaining a list of Instruments with their IDs](https://github.com/mnipper/AndroidSurvey/wiki/Obtaining-a-list-of-Instruments-with-their-IDs)
* [Passing Metadata When Launching a Survey From Another App](https://github.com/mnipper/AndroidSurvey/wiki/Passing-Metadata-When-Launching-a-Survey-From-Another-App)
* [Requiring Login to Begin a Survey](https://github.com/mnipper/AndroidSurvey/wiki/Survey-Login)
* [Rules](https://github.com/mnipper/AndroidSurvey/wiki/Rules)

## ActiveAndroid
[ActiveAndroid](https://github.com/pardom/ActiveAndroid) is used for database access.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

If you are interested in contributing, please read through the wiki first.  New question types are especially welcome!  See this [wiki page](https://github.com/mnipper/AndroidSurvey/wiki/Adding-a-Question-Type) for instructions.

If you are creating another app using this as a base project, be sure to [rename the package](https://github.com/mnipper/AndroidSurvey/wiki/Creating-Another-App).
