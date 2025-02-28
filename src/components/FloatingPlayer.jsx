import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import React, { useState } from 'react';
import { useSharedValue } from 'react-native-reanimated';
import { useNavigation } from '@react-navigation/native';
import { Slider } from 'react-native-awesome-slider';

import { NextButton, PlayPauseButton, PreviousButton } from './PlayControls';
import MovingText from './MovingText';

import { colors } from '../constant/colors';
import { spacing, fontSize } from '../constant/dimensions';
import { fontFamilies } from '../constant/fonts';

const imageUrl = "https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/643/325x325/karma-1709859652-YtrQEhSzIV.jpg";

const FloatingPlayer = () => {
    const navigation = useNavigation();
    const progress = useSharedValue(0.2);
    const [progressVal, setProgressVal] = useState(0.2);
    const min = useSharedValue(0);
    const max = useSharedValue(1);

    const handleOpenPlayerScreen = () => {
        navigation.navigate('PLAYER_SCREEN');
    }

    return (
        <View>
            <View>
                <Slider
                    style={{zIndex: 10}}
                    progress={progress}
                    value={progress.value}
                    onValueChange={(value) => {
//                         console.log(`Slider value changed to: ${value}`);
                        progress.value = value;
                        setProgressVal(value);
                    }}
                    minimumValue={min}
                    maximumValue={max}
                    theme={{
                        maximumTrackTintColor: colors.progressPastColor,
                        minimumTrackTintColor: colors.progressFutureColor,
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
                                <Text>{progressVal}</Text>
                        </View>}
                />
            </View>
            <TouchableOpacity style={styles.container} onPress={handleOpenPlayerScreen}>
                <Image
                    source={{uri: imageUrl}}
                    style={styles.coverImage}
                    zIndex={5}
                />
                <View style={styles.titleContainer}>
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
        fontSize: fontSize.lg,
        fontFamily: fontFamilies.reg,
        paddingRight: spacing.xl,
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
        fontSize: fontSize.md,
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