import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { FantasyTrackerSharedModule } from 'app/shared/shared.module';
import { FantasyTrackerCoreModule } from 'app/core/core.module';
import { FantasyTrackerAppRoutingModule } from './app-routing.module';
import { FantasyTrackerHomeModule } from './home/home.module';
import { FantasyTrackerEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    FantasyTrackerSharedModule,
    FantasyTrackerCoreModule,
    FantasyTrackerHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    FantasyTrackerEntityModule,
    FantasyTrackerAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class FantasyTrackerAppModule {}
