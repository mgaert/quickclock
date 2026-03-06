import { Component, OnInit, OnDestroy } from '@angular/core';
import { CommonModule } from '@angular/common';
import { WorkTimeService, WorkTimeEntry } from './services/worktime.service';
import { Subject } from 'rxjs';
import { takeUntil } from 'rxjs/operators';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css'
})
export class AppComponent implements OnInit, OnDestroy {
  currentTime = '--:--';
  todayLogs: WorkTimeEntry[] = [];
  status: string = 'ready';

  private destroy$ = new Subject<void>();
  private timeInterval: any;

  constructor(private workTimeService: WorkTimeService) {}

  ngOnInit(): void {
    this.updateTime();
    this.timeInterval = setInterval(() => this.updateTime(), 10000);

    this.workTimeService.todayLogs$
      .pipe(takeUntil(this.destroy$))
      .subscribe(logs => {
        this.todayLogs = logs;
        this.updateStatus();
      });
  }

  ngOnDestroy(): void {
    if (this.timeInterval) {
      clearInterval(this.timeInterval);
    }
    this.destroy$.next();
    this.destroy$.complete();
  }

  private updateTime(): void {
    const now = new Date();
    const hours = String(now.getHours()).padStart(2, '0');
    const minutes = String(now.getMinutes()).padStart(2, '0');
    this.currentTime = `${hours}:${minutes}`;
  }

  private updateStatus(): void {
    this.status = this.workTimeService.getStatus();
  }

  async recordKommen(): Promise<void> {
    await this.workTimeService.recordKommen();
  }

  async recordGehen(): Promise<void> {
    await this.workTimeService.recordGehen();
  }

  async deleteEntry(id: string): Promise<void> {
    await this.workTimeService.deleteEntry(id);
  }

  async resetDay(): Promise<void> {
    if (confirm('Alle Einträge von heute löschen?')) {
      await this.workTimeService.resetToday();
    }
  }

  exportCSV(): void {
    const csv = this.workTimeService.exportToCSV();
    const blob = new Blob([csv], { type: 'text/csv' });
    const url = window.URL.createObjectURL(blob);
    const a = document.createElement('a');
    a.href = url;
    const today = new Date().toISOString().split('T')[0];
    a.download = `arbeitszeit_${today}.csv`;
    a.click();
    window.URL.revokeObjectURL(url);
  }

  getStatusLabel(): string {
    switch (this.status) {
      case 'working':
        return '🟢 Im Dienst';
      case 'off':
        return '🔴 Dienst beendet';
      default:
        return 'Bereit zum Starten';
    }
  }

  getStatusClass(): string {
    return `status ${this.status}`;
  }
}
