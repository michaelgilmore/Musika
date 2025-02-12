import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import React from 'react';
import { useSharedValue } from 'react-native-reanimated';
import { Slider } from 'react-native-awesome-slider';

import { NextButton, PlayPauseButton, PreviousButton } from './PlayControls';
import MovingText from './MovingText';

import { colors } from '../constant/colors';
import { spacing } from '../constant/dimensions';

const imageUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/643/325x325/karma-1709859652-YtrQEhSzIV.jpg";

const FloatingPlayer = () => {
    const progress = useSharedValue(0.2);
    const min = useSharedValue(0);
    const max = useSharedValue(1);

    return (
        <View>
            <View>
                <Slider
                    style={{zIndex: 10}}
                    progress={progress}
                    value={progress.value}
                    onValueChange={(value) => progress.value = value}
                    minimumValue={min}
                    maximumValue={max}
                    theme={{
                        maximumTrackTintColor: "#ffffff",
                        minimumTrackTintColor: "#0000ff",
                    }}
                    containerStyle={{height: 6}}
                    renderBubble={() =>
                        <View style={{
                            width: 50,
                            height: 20,
                            backgroundColor: 'white',
                            borderRadius: 4,
                            alignItems: 'center',
                            justifyContent: 'center'}}>
                                <Text>{progress.value}</Text>
                        </View>}
                />
            </View>
            <TouchableOpacity style={styles.container}>
                <Image
                    source={{uri: imageUrl}}
                    style={styles.coverImage}
                    zIndex={5}
                />
                <View style={styles.nameContainer}>
{/*                     <Text style={styles.title}>Song Name</Text> */}
                    <MovingText
                        text="This is a name that is long enough"
                        animationThreshold={20}
                        style={styles.title}
                        zIndex={0}
                    />
                    <Text style={styles.artist}>Artist Name</Text>
                </View>
                <View style={styles.playControls} zIndex={5}>
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
    titleContainer: {
        flex: 1,
        paddingHorizontal: spacing.sm,
        overflow: 'hidden',
        marginLeft: spacing.sm,
        marginRight: spacing.lg,
    },
    artist: {
        color: colors.textSecondary,
    },
    nameContainer: {
        marginLeft: 16,
    },
    playControls: {
        flex: 1,
        flexDirection: 'row',
        justifyContent: 'flex-end',
        paddingHorizontal: 20,
        gap: 10,
    },
});