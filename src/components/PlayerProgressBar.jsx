import { StyleSheet, Text, View } from 'react-native'
import React, { useState } from 'react';
import { useSharedValue } from 'react-native-reanimated';

import { colors } from '../constant/colors';
import { iconSizes, spacing } from '../constant/dimensions';
import { fontFamilies } from '../constant/fonts';
import { fontSize } from '../constant/dimensions';

import { Slider } from 'react-native-awesome-slider';

const PlayerProgressBar = () => {
    const progress = useSharedValue(0.2);
    const [progressVal, setProgressVal] = useState(0.2);
    const min = useSharedValue(0);
    const max = useSharedValue(1);

    return (
        <View>
            <View style={styles.progressBar}>
                <Text style={styles.timeText}>00:00</Text>
                <Text style={styles.timeText}>00:00</Text>
            </View>
            <View style={styles.sliderContainer}>
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
                    containerStyle={styles.sliderStyle}
                    renderBubble={() => null}
                />
            </View>
        </View>
    )
}

export default PlayerProgressBar;

const styles = StyleSheet.create({
    progressBar: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
        marginVertical: spacing.md,
    },
    timeText: {
        color: colors.textPrimary,
        fontFamily: fontFamilies.reg,
        fontSize: fontSize.md,
        opacity: 0.75,
    },
    sliderContainer: {
    },
    sliderStyle: {
        height: 6,
        borderRadius: 10,
    },
})