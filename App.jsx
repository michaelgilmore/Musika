import { GestureHandlerRootView } from 'react-native-gesture-handler';
import {StyleSheet, Text, View} from 'react-native';
import React from 'react';
import { NavigationContainer } from '@react-navigation/native';

import StackNavigation from './src/navigation/StackNavigation';


const App = () => {
  return (
      <GestureHandlerRootView style={{flex: 1}}>
          <NavigationContainer>
              <StackNavigation />
          </NavigationContainer>
      </GestureHandlerRootView>
  );
}

export default App;

const styles = StyleSheet.create({})