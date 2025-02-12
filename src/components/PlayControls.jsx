import {TouchableOpacity} from 'react-native';
import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';

import { colors } from '../constant/colors';

export const PreviousButton = () => {
    return (
        <TouchableOpacity activeOpacity={0.75}>
            <MaterialCommunityIcons name="skip-previous-outline" size={40} color={colors.iconPrimary} />
        </TouchableOpacity>
    );
}

export const PlayPauseButton = () => {
    const isPlaying = false;

    return (
        <TouchableOpacity activeOpacity={0.75}>
            <MaterialCommunityIcons name={isPlaying ? "play-circle-outline" : "pause-circle-outline"} size={50} color={colors.iconPrimary} />
        </TouchableOpacity>
    );
}

export const NextButton = () => {
    return (
        <TouchableOpacity activeOpacity={0.75}>
            <MaterialCommunityIcons name="skip-next-outline" size={40} color={colors.iconPrimary} />
        </TouchableOpacity>
    );
}
