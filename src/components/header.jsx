import {StyleSheet, Text, TouchableOpacity, View} from 'react-native';
import React from 'react';

import FontAwesome5 from 'react-native-vector-icons/FontAwesome5';
import AntDesign from 'react-native-vector-icons/AntDesign';

import { colors } from '../constant/colors';
import { iconSizes, spacing } from '../constant/dimensions';

const Header = () => {
    return (
        <View style={styles.header}>
            <TouchableOpacity>
                <FontAwesome5 name={"grip-lines"} size={iconSizes.md} color={colors.iconPrimary} />
            </TouchableOpacity>

            <TouchableOpacity>
                <AntDesign name={"search1"} size={iconSizes.md} color={colors.iconPrimary} />
            </TouchableOpacity>
        </View>
    );
}

export default Header;

const styles = StyleSheet.create({
    header: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        paddingVertical: spacing.md,
        paddingHorizontal: spacing.lg,
    },
});