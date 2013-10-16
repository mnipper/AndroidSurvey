AndroidSurvey
=============
A survey application for Android that will soon have a Rails back-end.  The Android app syncs instruments and survey results automatically when internet is detected on the Android device.  This application is being designed for conducting research-oriented surveys.  This application is being developed at Duke University in a joint effort between adaptlab and CHPIR.

## Seeding the database

Set the DO_SEEDING variable to true in DatabaseSeed.java and start the application.  This will add an instrument each time the app starts.  When you are happy with your database, set DO_SEEDING to false.

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
