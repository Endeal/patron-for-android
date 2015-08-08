#!/usr/bin/env bash

# Update package manager
apt-get update
apt-get upgrade

# Install GNU Screen
apt-get install -y screen

# Install JDK & JRE
apt-get install -y openjdk-7-jre
apt-get install -y openjdk-7-jdk
export JAVA_HOME='/usr/lib/jvm/java-7-openjdk-amd64'

# Install Android Tools
apt-get install -y android-tools-adb
apt-get install -y android-tools-adbd
apt-get install lib32stdc++6 lib32z1

# Download Android SDK
wget http://dl.google.com/android/android-sdk_r24.3.3-linux.tgz -O android-sdk.tgz
tar zxvf android-sdk.tgz -C /opt/
rm android-sdk.tgz

# Install Build Tools, Android 5.1.1 and 2.3.3, x86 images, Google Play Services, and support repositories
/opt/android-sdk-linux/tools/android update sdk -u -a -t 1,3,5,24,37,68,99,100,139,140,145,146,150,151

# Install Gradle
sudo add-apt-repository ppa:cwchien/gradle
sudo apt-get update
sudo apt-get install -y gradle

# Install Android Studio
apt-add-repository ppa:paolorotolo/android-studio
apt-get update
apt-get install -y android-studio

if ! [ -L /var/www ]; then
  rm -rf /var/www
  ln -fs /vagrant /var/www
fi
