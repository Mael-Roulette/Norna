import { Tab, TabContent, TabList, TabPanel, Tabs } from '@angular/aria/tabs';
import { Component } from '@angular/core';
import { AppearanceSettings } from './components/appearance-settings/appearance-settings';
import { ProfileSettings } from './components/profile-settings/profile-settings';

@Component({
  imports: [TabList, Tab, Tabs, TabPanel, TabContent, ProfileSettings, AppearanceSettings],
  selector: 'app-settings',
  styleUrl: './settings.css',
  templateUrl: './settings.html',
})
export class Settings {}
