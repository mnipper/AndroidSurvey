AndroidSurvey
=============
A survey application for Android that will soon have a Rails back-end.  The Android app syncs instruments and survey results automatically when internet is detected on the Android device.

## Seeding the database

Set the DO_SEEDING variable to true in DatabaseSeed.java and start the application.  This will add an instrument each time the app starts.  When you are happy with your database, set DO_SEEDING to false.

## ActiveAndroid
[ActiveAndroid](https://github.com/pardom/ActiveAndroid) is used for database access.

## Contributing

1. Fork it
2. Create your feature branch (`git checkout -b my-new-feature`)
3. Commit your changes (`git commit -am 'Add some feature'`)
4. Push to the branch (`git push origin my-new-feature`)
5. Create new Pull Request

New question types are especially welcome!  See the wiki for instructions.
