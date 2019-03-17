# Bloom input method
Keyboards where developed to move the hammers of a typewriter to the paper using physical buttons.
The physical buttons have gone; we are now staring at the screen to find these small buttons that
are ordered so that the hammers of the typewriter do not cling together.

Bloom is designed fom scratch to function optimally on touch screens. It allows the user to focus
on the written text, not on the writing itself. To get started Bloom does not require leaning
anything by heart. Writing using Bloom may be a bit slower than using a conventional keyboard. However,
Bloom allows a proficient user to concentrate on what is written rather than on co-ordinating the fingers
on a tiny screen.

Bloom also may have a potential for users that suffer from disabilities that inhibit co-ordination or
cause tremor in fingers.

Bloom has been available in the [Google Play Store](https://play.google.com/store/apps/details?id=fi.knaap.bloom)
 for a long time. Since march 2019 the source code has been published here to GiHub as open source
 under the [MIT license](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/LICENSE)

# How does Bloom work

## Overview
Bloom defines a very simple language consisting of only 3 different pen strokes and writing a dot.
Depending on the direction of the stroke, the meaning of the stroke is a different letter. The strokes
are the simplest possible:
* A straight stroke
* A slightly curved stroke
* A stroke with a loop in the middle

By writing these strokes in different directions, different letters are selected. 8 directions are used,
creating 45 degree sectors. For curve and looped stroke, also the side of the cure or loop affects the
selection of the letter. Bloom contains a display that shows the user to which stroke each letter is
assigned.

## Writing
### Short stroke
The simplest stroke is a short stroke, just about the size from the centre of the circle to the inne
ring of letters. The direction of the short stroke determines the letter you are writing.

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/shortStroke.gif "Short stroke")

### Long stroke
A stroke that is lonnger will produce the letter on the middle of the group of three letters.
Again, ther direction of the stroke determines the letter you are writing.

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/longStroke.gif "Long stroke")

### Hooked strokes
If you make a stoke in a direction and contiue for the same distance 90 degrees left or right will produce
The letter left or right from the one in the middle. The hook doesn't need to be sharp.

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/leftHook.gif "Left hook")
![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/rightHook.gif "Right hook")

### Backspace
You can erase the previous character by giving only a short tap.

### Write anywhere
Since the writing is dependent only on the direction and the shape of the stroke, location is not very
important.You can write anywhere in the Bloom area. This makes it possible to write without seeing
where your finger excactly is.

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/writeAnywhere.gif "Write anywhere")

### Capitals, numbers and special characters
Bloom provides almost all but the basic characters in different modes. You can change mode by drawing
a circle.

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/changeMode.gif "changeMode")

# Installing and configuration

## Installation
Bloom can be installed to Android devices from the [Google Play Store](https://play.google.com/store/apps/details?id=fi.knaap.bloom)

## Android configuration
Since the input method integrates seemlessly with Andtoid, you need to tell android that you want to
use Bloom. You do that in the Android settings

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/AndroidSettingsEnableBloom.gif "Enable Bloom in android settings")

To actually use Bloom, you can enable the keyboard botton. This will add a small button that allows
you to easily switch between your old keyboard and Bloom, even in the middle of a word.

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/AndroidSettingShowKeyboardButton.gif "Enable keyboard button")

Now you will be able to switch between keyboards easily

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/changeInputMethod.gif "Change input method")

## Bloom configuration
You can configure Bloom to fit your needs. You can open the configuration screen from the Bloom application

![alt text](https://github.com/ArthurvdKnaap/input-method-bloom/blob/master/readmeFiles/bloomSettings.gif "Open Bloom settings")

The following items can be configured
- **Auto-capitalization** If enabled, will return always to normal mode after one capital has been written
- **Language** Choosen language impacts the letters that are offerred. E.g. Finnish language provides
the ö, ä and å- characters.
- **Font** Font in which the letters on Bloom are shown. This does not affect what you write.
- **Size** Determines what size the circle of letters is shown, also affects the length of the short stroke.

# Privacy Policy
Arthur van der Knaap built the Bloom app as an Open Source app. This SERVICE is provided no cost and
is intended for use as is. The service does not collect any data.
