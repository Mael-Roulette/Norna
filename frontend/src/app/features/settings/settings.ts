import { Tab, TabContent, TabList, TabPanel, Tabs } from '@angular/aria/tabs';
import { Component, inject } from '@angular/core';
import { UserService } from '../../service/user/user.service';
import { ProfileSettings } from './components/profile-settings/profile-settings';
import { AppearanceSettings } from './components/appearance-settings/appearance-settings';

@Component({
  imports: [TabList, Tab, Tabs, TabPanel, TabContent, ProfileSettings, AppearanceSettings],
  selector: 'app-settings',
  styleUrl: './settings.css',
  templateUrl: './settings.html',
})
export class Settings {}
