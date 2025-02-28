import {Image, StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import React from 'react'
import AntDesign from 'react-native-vector-icons/AntDesign';
import Feather from 'react-native-vector-icons/Feather';

import { colors } from '../constant/colors';
import { fontSize } from '../constant/dimensions';
import { fontFamilies } from '../constant/fonts';
import { iconSizes, spacing } from '../constant/dimensions';

import PlayerProgressBar from '../components/PlayerProgressBar';
import PlayerRepeatToggle from '../components/PlayerRepeatToggle';
import { NextButton, PlayPauseButton, PreviousButton } from '../components/PlayControls';

const imageUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/644/325x325/pretty-afternoon-1709859658-TKAtqZGQtZ.jpg";

const PlayerScreen = () => {
  const isLiked = false;
  const isMuted = false;
  return (
    <View style={styles.container}>
        {/* header */}
      <View style={styles.headerContainer}>
          <TouchableOpacity>
              <AntDesign name="arrowleft" size={iconSizes.lg} color={colors.iconPrimary} />
          </TouchableOpacity>
          <Text style={styles.headerText}>Now Playing</Text>
      </View>
      {/* image */}
      <View style={styles.coverImageContainer}>
        <Image source={{uri: imageUrl}} style={styles.coverImage} />
      </View>
      {/* title and artist */}
      <View style={styles.titleRowHeartContainer}>
          <View style={styles.titleContainer}>
              <Text style={styles.title}>Pretty Afternoon</Text>
              <Text style={styles.artist}>Imagine Dragons</Text>
          </View>
          <TouchableOpacity>
              <AntDesign name={isLiked?"heart":"hearto"} size={iconSizes.md} color={colors.iconSecondary} />
          </TouchableOpacity>
      </View>
      {/* player controls */}
      <View style={styles.playerControlsContainer}>
          <TouchableOpacity style={styles.volumeWrapper}>
              <Feather name={isMuted?"volume-x":"volume-1"} size={iconSizes.lg} color={colors.iconSecondary} />
          </TouchableOpacity>
          <View style={styles.repeatShuffleWrapper}>
              <PlayerRepeatToggle />
              <Feather name="shuffle" size={iconSizes.md} color={colors.iconSecondary} />
          </View>
      </View>
      {/* slider */}
      <View style={styles.sliderContainer}>
          <PlayerProgressBar />
      </View>
      {/* controls */}
      <View style={styles.controlsContainer}>
        <PreviousButton />
        <PlayPauseButton />
        <NextButton />
      </View>
    </View>
  )
}

export default PlayerScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        backgroundColor: colors.background,
        paddingHorizontal: spacing.lg,
        paddingTop: spacing.lg,
    },
    headerContainer: {
        width: "100%",
        flexDirection: 'row',
        alignItems: 'center',
    },
    headerText: {
        fontSize: fontSize.lg,
        color: colors.textPrimary,
        fontFamily: fontFamilies.bold,
        flex: 1,
        textAlign: "center",
    },
    coverImage: {
        height: 300,
        width: 300,
        borderRadius: 10,
    },
    coverImageContainer: {
        alignItems: "center",
        justifyContent: "center",
        marginVertical: spacing.lg,
    },
    titleContainer: {
        flex: 1,
        alignItems: 'center',
    },
    title: {
        fontSize: fontSize.xl,
        color: colors.textPrimary,
        fontFamily: fontFamilies.bold,
    },
    artist: {
        fontSize: fontSize.md,
        color: colors.textSecondary,
    },
    titleRowHeartContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'space-between',
    },
    playerControlsContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        marginVertical: spacing.lg,
    },
    volumeWrapper: {
        flex: 1,
    },
    repeatShuffleWrapper: {
        flexDirection: 'row',
        alignItems: 'center',
        gap: spacing.md,
    },
    controlsContainer: {
        flexDirection: 'row',
        alignItems: 'center',
        justifyContent: 'center',
        marginVertical: spacing.lg,
        gap: spacing.xl,
        marginVertical: spacing.xxl,
    },
})