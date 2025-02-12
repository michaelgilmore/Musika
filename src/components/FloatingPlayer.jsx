import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import React from 'react';
import { useSharedValue } from 'react-native-reanimated';
import { Slider } from 'react-native-awesome-slider';

import { NextButton, PlayPauseButton, PreviousButton } from './PlayControls';

import { colors } from '../constant/colors';

const imageUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/643/325x325/karma-1709859652-YtrQEhSzIV.jpg";

const FloatingPlayer = () => {
    const progress = useSharedValue(50);
    const min = useSharedValue(0);
    const max = useSharedValue(100);

//     disableMinTrackTintColor: ?
//     maximumTrackTintColor: after thumb
//     minimumTrackTintColor: before thumb
//     cacheTrackTintColor: ?
//     bubbleBackgroundColor: ?
//     heartbeatColor: ?

    return (
        <View>
            <View>
                <Slider
                    style={{zIndex: 10}}
                    progress={progress}
                    minimumValue={min}
                    maximumValue={max}
                    theme={{
                        disableMinTrackTintColor: "#FF0000",
                        maximumTrackTintColor: "#ffffff",
                        minimumTrackTintColor: "#0000ff",
                        cacheTrackTintColor: "#00ff00",
                        bubbleBackgroundColor: "#0000ff",
                        heartbeatColor: "#00ff00",
                    }}
                    containerStyle={{height: 6}}
                />
            </View>
            <TouchableOpacity style={styles.container}>
                <Image source={{uri: imageUrl}} style={styles.coverImage} />
                <View style={styles.nameContainer}>
                    <Text style={styles.title}>Song Name</Text>
                    <Text style={styles.artist}>Artist Name</Text>
                </View>
                <View style={styles.playControls}>
                    <PreviousButton />
                    <PlayPauseButton />
                    <NextButton />
                </View>
            </TouchableOpacity>
        </View>
    );
}

export default FloatingPlayer;

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        alignItems: 'center',
        paddingVertical: 10,
        paddingHorizontal: 10,
        backgroundColor: colors.background,
    },
    coverImage: {
        height: 70,
        width: 70,
    },
    title: {
        color: colors.textPrimary,
    },
    artist: {
        color: colors.textSecondary,
    },
    nameContainer: {
        marginLeft: 10,
    },
    playControls: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'flex-end',
        paddingHorizontal: 20,
        gap: 10,
    },
});