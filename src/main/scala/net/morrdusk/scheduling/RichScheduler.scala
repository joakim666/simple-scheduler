package net.morrdusk.scheduling

import org.quartz._


class RichScheduler(s: Scheduler) {
  def <->(job: JobDetail, trigger: Trigger) = s.scheduleJob(job, trigger)

  def ->>(job: JobDetail, trigger: Trigger) {
    (<->(job: JobDetail, trigger: Trigger)); +this
  }

  def <<-(triggerKey: TriggerKey) = s.unscheduleJob(triggerKey)

  def unary_+() {
    s start()
  }

  def <<() {
    s shutdown()
  }

  def unary_-() {
    s shutdown true
  }
}
