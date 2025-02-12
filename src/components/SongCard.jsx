import { Image, StyleSheet, Text, TouchableOpacity, View } from 'react-native';
import React from 'react';

import { colors } from '../constant/colors';
import { fontSize, spacing } from '../constant/dimensions';

const imageUrl = 'https://ncsmusic.s3.eu-west-1.amazonaws.com/tracks/000/001/644/325x325/pretty-afternoon-1709859658-TKAtqZGQtZ.jpg';

const SongCard = () => {
    return (
        <TouchableOpacity style={styles.container}>
            <Image source={{uri: imageUrl}} style={styles.coverImage} />
            <Text style={styles.songName}>Song Name</Text>
            <Text style={styles.artistName}>Artist Name</Text>
        </TouchableOpacity>
    );
}

export default SongCard;

const styles = StyleSheet.create({
    container: {
        height: 320,
        width: 250,
    },
    coverImage: {
        width: 250,
        height: 250,
        borderRadius: 10,
    },
    songName: {
        color: colors.textPrimary,
        fontSize: fontSize.md,
        textAlign: 'center',
        paddingVertical: spacing.sm,
    },
    artistName: {
        color: colors.textSecondary,
        fontSize: fontSize.sm,
        textAlign: 'center',
    }
});