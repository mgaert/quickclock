import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';
import { Preferences } from '@capacitor/preferences';

export interface WorkTimeEntry {
  id: string;
  type: 'kommen' | 'gehen';
  time: string;
  timestamp: number;
  date: string;
}

export interface DayLog {
  date: string;
  entries: WorkTimeEntry[];
}

@Injectable({
  providedIn: 'root'
})
export class WorkTimeService {
  private readonly STORAGE_KEY = 'worktime_logs';
  private todayLogsSubject = new BehaviorSubject<WorkTimeEntry[]>([]);
  public todayLogs$ = this.todayLogsSubject.asObservable();

  constructor() {
    this.loadTodayLogs();
  }

  private getTodayKey(): string {
    const today = new Date();
    return today.toISOString().split('T')[0];
  }

  private async loadTodayLogs(): Promise<void> {
    try {
      const result = await Preferences.get({ key: this.STORAGE_KEY });
      const allLogs = result.value ? JSON.parse(result.value) : {};
      const todayKey = this.getTodayKey();
      const todayLogs = allLogs[todayKey] || [];
      this.todayLogsSubject.next(todayLogs);
    } catch (error) {
      console.error('Error loading logs:', error);
      this.todayLogsSubject.next([]);
    }
  }

  private async saveLogs(logs: WorkTimeEntry[]): Promise<void> {
    try {
      const result = await Preferences.get({ key: this.STORAGE_KEY });
      const allLogs = result.value ? JSON.parse(result.value) : {};
      const todayKey = this.getTodayKey();
      allLogs[todayKey] = logs;
      await Preferences.set({
        key: this.STORAGE_KEY,
        value: JSON.stringify(allLogs)
      });
      this.todayLogsSubject.next(logs);
    } catch (error) {
      console.error('Error saving logs:', error);
    }
  }

  async recordKommen(): Promise<void> {
    const now = new Date();
    const entry: WorkTimeEntry = {
      id: Date.now().toString(),
      type: 'kommen',
      time: now.toLocaleTimeString('de-DE'),
      timestamp: now.getTime(),
      date: this.getTodayKey()
    };

    const currentLogs = this.todayLogsSubject.value;
    await this.saveLogs([...currentLogs, entry]);
    this.playSound();
  }

  async recordGehen(): Promise<void> {
    const now = new Date();
    const entry: WorkTimeEntry = {
      id: Date.now().toString(),
      type: 'gehen',
      time: now.toLocaleTimeString('de-DE'),
      timestamp: now.getTime(),
      date: this.getTodayKey()
    };

    const currentLogs = this.todayLogsSubject.value;
    await this.saveLogs([...currentLogs, entry]);
    this.playSound();
  }

  async deleteEntry(id: string): Promise<void> {
    const currentLogs = this.todayLogsSubject.value;
    const filtered = currentLogs.filter(log => log.id !== id);
    await this.saveLogs(filtered);
  }

  async resetToday(): Promise<void> {
    await this.saveLogs([]);
  }

  getTodayLogs(): WorkTimeEntry[] {
    return this.todayLogsSubject.value;
  }

  getStatus(): string {
    const logs = this.todayLogsSubject.value;
    if (logs.length === 0) {
      return 'ready';
    }
    const lastLog = logs[logs.length - 1];
    return lastLog.type === 'kommen' ? 'working' : 'off';
  }

  private playSound(): void {
    try {
      const audioContext = new (window.AudioContext || (window as any).webkitAudioContext)();
      const oscillator = audioContext.createOscillator();
      const gain = audioContext.createGain();

      oscillator.connect(gain);
      gain.connect(audioContext.destination);

      oscillator.frequency.value = 800;
      oscillator.type = 'sine';

      gain.gain.setValueAtTime(0.3, audioContext.currentTime);
      gain.gain.exponentialRampToValueAtTime(0.01, audioContext.currentTime + 0.1);

      oscillator.start(audioContext.currentTime);
      oscillator.stop(audioContext.currentTime + 0.1);
    } catch (error) {
      console.error('Error playing sound:', error);
    }
  }

  exportToCSV(): string {
    const logs = this.todayLogsSubject.value;
    const date = this.getTodayKey();
    let csv = 'Datum,Typ,Uhrzeit\n';
    logs.forEach(log => {
      csv += `${date},${log.type},${log.time}\n`;
    });
    return csv;
  }

  calculateWorkedHours(): { hours: number; minutes: number; total: number } {
    const logs = this.todayLogsSubject.value;
    let totalMinutes = 0;

    for (let i = 0; i < logs.length; i += 2) {
      const kommenLog = logs[i];
      const gehenLog = logs[i + 1];

      if (kommenLog && kommenLog.type === 'kommen' && gehenLog && gehenLog.type === 'gehen') {
        const diffMs = gehenLog.timestamp - kommenLog.timestamp;
        const diffMinutes = diffMs / (1000 * 60);
        totalMinutes += diffMinutes;
      }
    }

    const hours = Math.floor(totalMinutes / 60);
    const minutes = Math.round(totalMinutes % 60);

    return {
      hours,
      minutes,
      total: totalMinutes
    };
  }

  getFormattedWorkedTime(): string {
    const { hours, minutes } = this.calculateWorkedHours();
    if (hours === 0 && minutes === 0) {
      return '0h 0m';
    }
    return `${hours}h ${minutes}m`;
  }
