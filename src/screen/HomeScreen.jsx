import {FlatList, StyleSheet, Text, View} from 'react-native';
import React from 'react';

import Header from '../components/header';
import CategoryList from '../components/CategoryList';
import FloatingPlayer from '../components/FloatingPlayer';
import SongCard from '../components/SongCard';

import { colors } from '../constant/colors';

const HomeScreen = () => {
    return (
        <View style={styles.container}>
            <Header />
            <FlatList
                data={[1, 2, 3, 4, 5]}
                renderItem={CategoryList}
                contentContainerStyle={{paddingBottom: 100}}
            />
            <FloatingPlayer />
        </View>
    );
}

export default HomeScreen;

const styles = StyleSheet.create({
    container: {
        backgroundColor: colors.background,
        flex: 1,
    },
})