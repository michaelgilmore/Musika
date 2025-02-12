import { StyleSheet, Text, View, FlatList } from 'react-native';
import React from 'react';

import SongCard from '../components/SongCard';

import { colors } from '../constant/colors';
import { fontFamilies } from '../constant/fonts';
import { fontSize, spacing } from '../constant/dimensions';

const CategoryList = () => {
    return (
        <View styles={styles.container}>
            <Text style ={styles.headingText}>Recommended for you</Text>
            <FlatList
                data={[1, 2, 3, 4, 5]}
                renderItem={SongCard}
                horizontal={true}
                ItemSeparatorComponent={() => <View style={{marginHorizontal: spacing.sm}} />}
                contentContainerStyle={{paddingHorizontal: spacing.lg}}
            />
        </View>
    );
}

export default CategoryList;

const styles = StyleSheet.create({
    container: {
        flex: 1,
    },
    headingText: {
        fontFamily: fontFamilies.bold,
        fontSize: fontSize.xl,
        color: colors.textPrimary,
        paddingHorizontal: spacing.lg,
        paddingVertical: spacing.md,
    }
});