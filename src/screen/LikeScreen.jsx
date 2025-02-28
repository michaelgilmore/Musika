import {FlatList, StyleSheet, Text, TouchableOpacity, View} from 'react-native'
import React from 'react'

import MaterialCommunityIcons from 'react-native-vector-icons/MaterialCommunityIcons';
import AntDesign from 'react-native-vector-icons/AntDesign';
import SimpleLineIcons from 'react-native-vector-icons/SimpleLineIcons';

import SongCard from '../components/SongCard';

import { colors } from '../constant/colors';
import { spacing, fontSize } from '../constant/dimensions';
import { fontFamilies } from '../constant/fonts';

const LikeScreen = () => {
    return (
        <View style={styles.container}>
            {/* header */}
            <View style={styles.headerContainer}>
                <TouchableOpacity>
                    <AntDesign name="arrowleft" size={35} color={colors.iconPrimary} />
                </TouchableOpacity>
                <TouchableOpacity>
                    <SimpleLineIcons name="equalizer" size={30} color={colors.iconPrimary} style={{ transform: [{ rotate: '90deg' }] }} />
                </TouchableOpacity>
            </View>
            <Text style={styles.headingText}>Liked Songs</Text>
            <FlatList
                data={[1, 2, 3, 4, 5]}
                numColumns={2}
                columnWrapperStyle={{
                    justifyContent: 'space-between',
                    marginHorizontal: spacing.sm,
                    marginVertical: spacing.sm,
                }}
                renderItem={() => <SongCard
                    containerStyle={{width: '45%'}}
                    imageStyle={{height:'150', width: '150'}}
                />}
             />
        </View>
    )
}

export default LikeScreen;

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        backgroundColor: colors.background,
    },
    headerContainer: {
        flexDirection: 'row',
        width: '100%',
        justifyContent: 'space-between',
        alignItems: 'center',
        paddingHorizontal: spacing.md,
        paddingVertical: spacing.md,
    },
    headingText: {
        fontSize: fontSize.xl,
        color: colors.textPrimary,
        fontFamily: fontFamilies.bold,
        padding: spacing.lg,
    },
})