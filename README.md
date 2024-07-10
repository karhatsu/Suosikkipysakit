# Suosikkipysäkit

Simple Android application for quickly showing the next departures for your favourite public transportation stop in Helsinki area.

The application can be downloaded from Google Play:
https://play.google.com/store/apps/details?id=com.karhatsu.suosikkipysakit

## Release instructions

1. Update `versionCode` and `versionName` in `app/build.gradle`
2. Commit the version change
3. `Build` - `Generate Signed App Bundle / APK`
4. Choose APK for testing with a phone and install with `apk install`
5. Choose ABB for releasing and upload to Google Play
6. `git tag VX.X && git push origin VX.X`
