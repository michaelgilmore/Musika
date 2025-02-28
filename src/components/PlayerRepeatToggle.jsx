import { StyleSheet, Text, TouchableOpacity, View } from 'react-native'
import React from 'react'
import Feather from 'react-native-vector-icons/Feather';

import { colors } from '../constant/colors';
import { iconSizes, spacing } from '../constant/dimensions';

const PlayerRepeatToggle = () => {
  return (
      <TouchableOpacity>
          <Feather name="repeat" size={iconSizes.md} color={colors.iconSecondary} />
      </TouchableOpacity>
  )
}

export default PlayerRepeatToggle;

const styles = StyleSheet.create({})