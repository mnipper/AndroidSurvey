AndroidSurvey
=============
A survey application for Android with a [rails backend](https://github.com/mnipper/rails_survey).  The Android app syncs instruments and survey results automatically when internet is detected on the Android device.  This application is being designed for conducting research-oriented surveys.  This application is being developed at Duke University in a joint effort between adaptlab and CHPIR.

## Documentation
* [Model Summary](https://github.com/mnipper/AndroidSurvey/wiki/Model-summary)
* [Design Overview](https://github.com/mnipper/AndroidSurvey/wiki/Design-Overview)
* [Adding a Question Type](https://github.com/mnipper/AndroidSurvey/wiki/Adding-a-Question-Type)
* [Syncing with a Remote Table](https://github.com/mnipper/AndroidSurvey/wiki/Syncing-with-Remote-Tables)

## Seeding the database

Set the SEED_DB metadata variable to true in AndroidManifest and start the application.  This will add an instrument each time the app starts.  When you are happy with your database, set SEED_DB to false.

Note: Currently this is set to only seed the database if being run in debug mode, and to ignore otherwise.  This is done to hopefully prevent seeding from making it into production.  Open to suggestions on how to better do this.

## ActiveAndroid
[ActiveAndroid](https://github.com/pardom/ActiveAndroid) is used for database access.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

If you are interested in contributing, please read through the wiki first.  New question types are especially welcome!  See this [wiki page](https://github.com/mnipper/AndroidSurvey/wiki/Adding-a-Question-Type) for instructions.
