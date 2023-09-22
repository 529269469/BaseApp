package com.example.psychology.ui

class Connotation {



    /**
     * 训练时整体身心状态
     */
    fun physical_and_mental_state(num: Int): String {
        if (num <= 40) {
            return "基于您在进行放松冥想训练时所搜集的HRV和EEG的数据分析得出，您的整体身心状态得分为${num}分，这表明您当前的身心状态不佳。请您回忆一下您是否正在经历一些心理压力或生理疲劳，是否经常感到紧张、焦虑和压力，或者是您的生活中出现了某些事件或变故。请您更多的留意自己的心理状况和生理感受，尝试在日常生活中实施一些放松策略，如深呼吸、冥想或温馨的瑜伽。如果您觉得压力过大，也请考虑寻求专业的心理咨询。"
        }
        if (num in 41..55) {
            return "基于您在进行放松冥想训练时所搜集的HRV和EEG的数据分析得出，您的整体身心状态得分为${num}分，这表明您当前的身心状态较差，这可能表明您有一些轻度的压力或疲劳，这可能会影响到您的情绪和专注力。请您更多的留意自己身体和心理放松的需要，并尝试其他的压力管理技巧。也请记住定期锻炼、保持充足的睡眠和采取健康的饮食习惯。"
        }
        if (num in 56..75) {
            return "基于您在进行放松冥想训练时所搜集的HRV和EEG的数据分析得出，您的整体身心状态得分为${num}分，这表明您当前的身心状态处于常态水平，这可能表明您在生活中一定程度上能够处理自己的压力或疲劳，您的情绪和专注力相对稳定，但仍有较大的进步空间。请您在工作生活中也要留意自己身体和心理放松的需要，培养规律的作息和身体锻炼，进一步提升良好的身心健康状态。"
        }
        if (num in 76..90) {
            return "基于您在进行放松冥想训练时所搜集的HRV和EEG的数据分析得出，您的整体身心状态得分为${num}分，这表明您当前的身心状态良好。您的情绪状态稳定，专注力和放松状态也非常良好。您应该会觉得精神状态较好，能有效地应对日常生活中的挑战。请继续保持当前健康的生活方式，包括规律的锻炼、良好的睡眠习惯以及营养均衡的饮食。同时，定期的放松训练也可以帮助你保持这种相对稳定的身心状态。"
        }
        if (num > 90) {
            return "基于您在进行放松冥想训练时所搜集的HRV和EEG的数据分析得出，您的整体身心状态得分为${num}分，这表明您当前的身心状态相当好。您的情绪状态稳定，专注力和放松状态也非常良好。这可能反映出您已经掌握了有效的压力管理策略和高度的精神集中能力。在这种状态下，您可能会发现自己对于新的挑战和任务有着非常好的适应性。这对您的整体健康和福祉是非常有益的。请继续保持这种状态。"
        }
        return ""
    }

    /**
     * 训练有效性
     */
    fun training_effectiveness(num: Int): String {
        if (num <= 40) {
            return "在这次训练中，您的训练有效性得分为${num}分。这可能表明您的心理和生理状态没有显著改善。产生这一结果的原因可能有很多，例如您受到了外界环境的干扰，或者该课程和您的需求不够匹配也可能时您还没有做好准备。我们建议您改善训练的内外环境，增加使用放松冥想训练系统的频率或者该学习其他视频课程。记住，改变需要时间，所以请耐心等待。"
        }
        if (num in 41..55) {
            return "在这次训练中，您的训练有效性得分为${num}分。这表明您并未较好的投入到放松冥想状态之中，您仍有较大提升空间。您可以尝试着改善训练环境，寻找适合自己的课程，并尝试将其与其他健康习惯结合，如规律锻炼、良好的睡眠和均衡饮食等，相信您一定会有更加显著的变化的！"
        }
        if (num in 56..75) {
            return "在这次训练中，您的训练有效性得分为${num}分。这意味着您的身心状态已经有所改善。但您的训练仍有较大的提升空间，您可以再多加尝试寻找适合自己的课程，您也可以试着将训练中的观点和理论更积极的应用到日常生活中，这将有助于您巩固训练成果。"
        }
        if (num in 76..90) {
            return "在这次训练中，您的训练有效性得分为${num}分。这表示您的训练已经开始产生了较好的效果。您可能已经觉得压力减轻，情绪更稳定，精神更为集中。这是一个很好的进步！为了持续改善，我们鼓励您继续定期进行训练，并保持健康的生活习惯。"
        }
        if (num > 90) {
            return "在这次训练中，您的训练有效性得分为${num}分。您的训练效果得分非常高，这表明您的训练效果显著。您的压力和疲劳程度可能大大降低，而放松和专注程度则可能大幅度提升。这是一个巨大的成功，但请记住，持续保持这种状态也同样重要。继续利用放松冥想训练系统来保持和进一步提升您的当前状态。"
        }
        return ""
    }

    /**
     * 前测-压力
     */
    fun before_measurement_pressure(num: Int): String {
        if (num <= 40) {
            return "<font color=\"#8A8989\"><b>·压力值：</b></font>在前测分析中您的压力值得分为${num}分，这可能表明您正在经历重大的压力。您可能会感到焦虑、紧张，身心疲劳，难以集中注意力。长时间的高压力状态可能对您的身心健康产生不利影响。"
        }
        if (num in 41..55) {
            return "<font color=\"#8A8989\"><b>·压力值：</b></font>在前测分析中您的压力值得分为${num}分，这意味着您可能正在经历比平时更大的压力。这可能会导致一些不适感，如心率加快、肌肉紧张、睡眠质量下降，甚至影响您的情绪和专注力。承认压力并寻找有效的应对方法是非常重要的。"
        }
        if (num in 56..75) {
            return "<font color=\"#8A8989\"><b>·压力值：</b></font>在前测分析中您的压力值得分为${num}分，这表示您正在经历正常的日常压力。在这种状态下，尽管您可能会偶尔感到紧张或焦虑，但您似乎已经掌握了处理压力的有效方法。"
        }
        if (num in 76..90) {
            return "<font color=\"#8A8989\"><b>·压力值：</b></font>在前测分析中您的压力值得分为${num}分，这表示你的压力水平相对较低。虽然您可能偶尔感到一些压力，但总体上您的调节能力良好。在这种状态下，您可能会觉得比较放松，身心都较为舒适。"
        }
        if (num > 90) {
            return "<font color=\"#8A8989\"><b>·压力值：</b></font>在前测分析中您的压力值得分为${num}分，这意味着您的压力水平很低。在这种状态下，您可能会觉得心情放松，精神焕发，日常活动和工作效率也可能有所提高。"
        }
        return ""
    }

    /**
     * 前测-情绪值
     */
    fun before_measurement_emotion(num: Int): String {
        val BEFORE_MEASUREMENT_EMOTION_40 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的情绪值得分为${num}分。这意味着您最近可能在经历一些负面情绪，如沮丧、疲惫或焦虑。这种状态可能影响您的日常功能和生活质量。"
        val BEFORE_MEASUREMENT_EMOTION_40_55 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的情绪值得分为${num}分，表示您可能会感到轻度的压力或焦虑。您可能会间歇性地感到情绪不稳定或易疲惫。"
        val BEFORE_MEASUREMENT_EMOTION_55_75 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的情绪值得分为${num}分，这表明您的情绪状态基本稳定。这是一个积极的信号，它表示您已经掌握了较好的情绪管理技巧。"
        val BEFORE_MEASUREMENT_EMOTION_75_90 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的情绪值得分为${num}分，这意味着您的情绪状态非常稳定，您能有效地处理日常生活中的压力和挑战。"
        val BEFORE_MEASUREMENT_EMOTION_90 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的情绪值得分为${num}分，这表明您的情绪状态非常稳定，应对压力和挑战的能力强。这是非常有益于您的整体健康和福祉的。"
        if (num <= 40) {
            return BEFORE_MEASUREMENT_EMOTION_40
        }
        if (num in 41..55) {
            return BEFORE_MEASUREMENT_EMOTION_40_55
        }
        if (num in 56..75) {
            return BEFORE_MEASUREMENT_EMOTION_55_75
        }
        if (num in 76..90) {
            return BEFORE_MEASUREMENT_EMOTION_75_90
        }
        if (num > 90) {
            return BEFORE_MEASUREMENT_EMOTION_90
        }
        return ""
    }

    /**
     * 前测-疲劳值
     */
    fun before_measurement_tired(num: Int): String {
        val BEFORE_MEASUREMENT_TIRED_40 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的疲劳值得分为${num}分，这可能表明您正在经历重大的身心疲劳。您可能会感到持续的疲累，难以集中注意力，甚至可能出现情绪波动。"
        val BEFORE_MEASUREMENT_TIRED_40_55 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的疲劳值得分为${num}分，这意味着您可能正在感受到一定程度的身心疲劳。这可能会影响您的日常功能、情绪和专注力。"
        val BEFORE_MEASUREMENT_TIRED_55_75 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的疲劳值得分为${num}分，这表明您的疲劳水平处于正常范围。适度的疲劳是正常的，而且有时候它甚至可以帮助我们更好地放松和恢复。"
        val BEFORE_MEASUREMENT_TIRED_75_90 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的疲劳值得分为${num}分，这表示你的疲劳水平相对较低。尽管您可能会感到轻微的疲劳，但总体上您的精力水平仍然不错。"
        val BEFORE_MEASUREMENT_TIRED_90 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的疲劳值得分为${num}分，这意味着您的身心状态处于良好和充满活力的状态。在这种状态下，您可能觉得精力充沛，清醒，并且在工作或学习中表现出色。"

        if (num <= 40) {
            return BEFORE_MEASUREMENT_TIRED_40
        }
        if (num in 41..55) {
            return BEFORE_MEASUREMENT_TIRED_40_55
        }
        if (num in 56..75) {
            return BEFORE_MEASUREMENT_TIRED_55_75
        }
        if (num in 76..90) {
            return BEFORE_MEASUREMENT_TIRED_75_90
        }
        if (num > 90) {
            return BEFORE_MEASUREMENT_TIRED_90
        }
        return ""
    }

    /**
     * 前测-专注度
     */
    fun before_measurement_concentration(num: Int): String {
        val BEFORE_MEASUREMENT_CONCENTRATION_40 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的专注度得分为${num}分。这可能意味着您最近可能存在注意力分散或难以集中注意力的情况。这可能会影响您的工作效率和学习能力。"
        val BEFORE_MEASUREMENT_CONCENTRATION_40_55 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的专注度得分为${num}分，表示您可能会觉得有一些分心。虽然这不会严重影响您的日常活动，但可能会在一定程度上降低您的工作或学习效率。"
        val BEFORE_MEASUREMENT_CONCENTRATION_55_75 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的专注度得分为${num}分，这表明您在很多时候能够集中注意力于当前的任务。这将有助于您更有效地完成工作和学习任务。"
        val BEFORE_MEASUREMENT_CONCENTRATION_75_90 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的专注度得分为${num}分，这意味着您的注意力集中，能够有效地关注到当前的任务或活动。这种状态将使您在解决问题、学习新知识或应对日常挑战时表现出色。"
        val BEFORE_MEASUREMENT_CONCENTRATION_90 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的专注度得分为${num}分，这表明您具有极高的专注力和集中精力的能力。您具有强大的抵御外界干扰以及内部无关想法的干扰，这将极大地提升您的工作效率和学习成果。"

        if (num <= 40) {
            return BEFORE_MEASUREMENT_CONCENTRATION_40
        }
        if (num in 41..55) {
            return BEFORE_MEASUREMENT_CONCENTRATION_40_55
        }
        if (num in 56..75) {
            return BEFORE_MEASUREMENT_CONCENTRATION_55_75
        }
        if (num in 76..90) {
            return BEFORE_MEASUREMENT_CONCENTRATION_75_90
        }
        if (num > 90) {
            return BEFORE_MEASUREMENT_CONCENTRATION_90
        }
        return ""
    }

    /**
     * 前测-放松度
     */
    fun before_measurement_relax(num: Int): String {
        val BEFORE_MEASUREMENT_RELAX_40 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的放松度得分为${num}分，您近期可能较为容易感受到焦虑或紧张情绪，并且较难进入深度放松或冥想状态。这可能会对您的睡眠质量产生影响，也有可能影响到您的日常活动和工作效率。"
        val BEFORE_MEASUREMENT_RELAX_40_55 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的放松度得分为${num}分，您可能会时不时感到有些压力，在遇到较为困难的任务或者挑战时容易感受到疲劳，或者发现自己难以专注。"
        val BEFORE_MEASUREMENT_RELAX_55_75 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的放松度得分为${num}分。您可能会有时感到平静放松，有时则会感到有些紧张。这是一个正常的状态，说明您可以有效地应对日常生活中的压力和挑战。"
        val BEFORE_MEASUREMENT_RELAX_75_90 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的放松度得分为${num}分，这表明您大部分时间都能保持平和宁静的状态，能很好地应对压力，并且在面对挑战时也能保持清晰的思绪。"
        val BEFORE_MEASUREMENT_RELAX_90 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的放松度得分为${num}分，这是一个非常理想的状态，意味着您能很好地控制和管理自己的压力，从而使得自己始终保持愉快和宁静的心境。"

        if (num <= 40) {
            return BEFORE_MEASUREMENT_RELAX_40
        }
        if (num in 41..55) {
            return BEFORE_MEASUREMENT_RELAX_40_55
        }
        if (num in 56..75) {
            return BEFORE_MEASUREMENT_RELAX_55_75
        }
        if (num in 76..90) {
            return BEFORE_MEASUREMENT_RELAX_75_90
        }
        if (num > 90) {
            return BEFORE_MEASUREMENT_RELAX_90
        }
        return ""
    }


    /**
     * 后测
     */
    fun aftertest_relax(num: Int,num2: Int): String {
        val AFTERTEST_RELAX_40 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的后测放松度为${num}分，训练效果得分为${num2}分。这可能意味着您在冥想过程中可能仍然感到一些焦虑或者难以放松，或者您在训练过程中受到某些外部环境的影响，导致训练效果不佳。"
        val AFTERTEST_RELAX_40_55 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的后测放松度为${num}分，训练效果得分为${num2}分。虽然您已经在尝试进行放松冥想训练，但可能还没有达到理想的效果。也许您在训练过程中受到某些外部环境的影响，导致训练效果不佳。"
        val AFTERTEST_RELAX_55_75 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的后测放松度为${num}分，训练效果得分为${num2}分。这说明您能够在一定程度上通过放松冥想来减轻压力，达到放松的效果。"
        val AFTERTEST_RELAX_75_90 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的后测放松度为${num}分，训练效果得分为${num2}分。这表明您的放松冥想效果很好，您可能已经感受到了放松冥想对你身心状态的积极影响，发现放松冥想能够有效地帮助您放松并降低压力。"
        val AFTERTEST_RELAX_90 =
            "<font color=\"#8A8989\"><b>·放松度：</b></font>您的后测放松度为${num}分，训练效果得分为${num2}分。这是一个非常理想的结果，显示您的训练效果优秀。您可能很明显的感受到放松冥想能有效地帮助您进入放松和宁静的状态，使你在接下来的工作和生活中更加轻松、自在和灵活。"

        if (num2 <= 40) {
            return AFTERTEST_RELAX_40
        }
        if (num2 in 41..55) {
            return AFTERTEST_RELAX_40_55
        }
        if (num2 in 56..75) {
            return AFTERTEST_RELAX_55_75
        }
        if (num2 in 76..90) {
            return AFTERTEST_RELAX_75_90
        }
        if (num2 > 90) {
            return AFTERTEST_RELAX_90
        }
        return ""
    }

    fun aftertest_concentration(num: Int,num2: Int): String {
        val AFTERTEST_CONCENTRATION_40 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的后测专注度为${num}分，训练效果得分为${num2}分。相较于其他人，在训练中您的专注度变化不大。如果您原来就比较容易分心，那当心灵进入放松冥想状态时，很容易被外部因素或内心的思维打扰。"
        val AFTERTEST_CONCENTRATION_40_55 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的后测专注度为${num}分，训练效果得分为${num2}分。您在进行放松冥想训练中的专注度变化幅度较小。虽然您已经开始了冥想训练，但专注力的提升可能还不太明显。您可以试着寻找一下是否有外界干扰，然后再次进行训练。"
        val AFTERTEST_CONCENTRATION_55_75 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的后测专注度为${num}分，训练效果得分为${num2}分。您的专注力在放松冥想训练后有所提高，但仍有进步的空间。在这个阶段，您可能已经开始感受到放松冥想对于减轻压力、提升专注力的积极影响。"
        val AFTERTEST_CONCENTRATION_75_90 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的后测专注度为${num}分，训练效果得分为${num2}分。这说明您通过放松冥想训练，已经显著提升了自己的专注力。在这个状态下，您可能发现自己在日常生活中的注意力更集中，处理问题的能力增强。"
        val AFTERTEST_CONCENTRATION_90 =
            "<font color=\"#8A8989\"><b>·专注度：</b></font>您的后测专注度为${num}分，训练效果得分为${num2}分。这是非常理想的结果，表明您成功地通过放松冥想训练显著提升了专注力。在这种状态下，您可能发现自己无论在工作还是在学习中，都能更好地集中精力，效率和质量有显著提升。"

        if (num2 <= 40) {
            return AFTERTEST_CONCENTRATION_40
        }
        if (num2 in 41..55) {
            return AFTERTEST_CONCENTRATION_40_55
        }
        if (num2 in 56..75) {
            return AFTERTEST_CONCENTRATION_55_75
        }
        if (num2 in 76..90) {
            return AFTERTEST_CONCENTRATION_75_90
        }
        if (num2 > 90) {
            return AFTERTEST_CONCENTRATION_90
        }
        return ""
    }

    fun aftertest_tired(num: Int,num2: Int): String {
        val AFTERTEST_TIRED_40 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的后测疲劳值为${num}分，训练效果得分为${num2}分。这可能意味着冥想训练对您的疲劳度改善并没有显著效果。训练时的外界影响，您的身心状态等都可能会影响训练效果。若您仍常常感到困倦、无精打采或者缺乏活力，请您尝试多种放松训练方法，并保持作息规律。"
        val AFTERTEST_TIRED_40_55 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的后测疲劳值为${num}分，训练效果得分为${num2}分。虽然您已经开始了放松冥想训练，但此训练可能还未对您的体验有明显的改善。您可以尝试多种训练方式结合，并且持续进行学习训练，持续不断的训练是改善的关键！"
        val AFTERTEST_TIRED_55_75 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的后测疲劳值为${num}分，训练效果得分为${num2}分。这意味着您可能已经感受到了您的疲劳程度在放松冥想训练后有所减轻，在接下来的工作任务中可能更具有活力。但请您持续进行联系，您还有很大的提升空间。"
        val AFTERTEST_TIRED_75_90 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的后测疲劳值为${num}分，训练效果得分为${num2}分。这表明您的放松冥想训练成效显著，疲劳程度已得到大幅度改善。您可能已经能察觉到自己的精神状态变得更好，身心都感觉更加轻松。"
        val AFTERTEST_TIRED_90 =
            "<font color=\"#8A8989\"><b>·疲劳值：</b></font>您的后测疲劳值为${num}分，训练效果得分为${num2}分。这是非常理想的结果，显示您成功地通过放松冥想训练显著地降低了疲劳感。在这种状态下，您可能发现自己无论在工作还是在学习中，都能更好地集中精力，效率和质量有显著提升。"

        if (num2 <= 40) {
            return AFTERTEST_TIRED_40
        }
        if (num2 in 41..55) {
            return AFTERTEST_TIRED_40_55
        }
        if (num2 in 56..75) {
            return AFTERTEST_TIRED_55_75
        }
        if (num2 in 76..90) {
            return AFTERTEST_TIRED_75_90
        }
        if (num2 > 90) {
            return AFTERTEST_TIRED_90
        }
        return ""
    }

    fun aftertest_emotion(num: Int,num2: Int): String {
        val AFTERTEST_EMOTION_40 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的后测情绪值为${num}分，训练效果得分为${num2}分。这可能意味着冥想训练对您的情绪改善并没有显著效果。外部环境的影响、您对放松冥想的敏感度等因素都有可能影响训练效果。如果您感到情绪波动较大，您可以控制好内部外部因素后再次尝试，或者搭配其他放松训练课程进行调节。"
        val AFTERTEST_EMOTION_40_55 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的后测情绪值为${num}分，训练效果得分为${num2}分。虽然您已经开始了冥想训练，但情绪的改善程度可能还不太明显。但是身心状态的改善需要持续的学习和训练，若您仍不满足于当前状态，请您持续地进行尝试和学习。"
        val AFTERTEST_EMOTION_55_75 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的后测情绪值为${num}分，训练效果得分为${num2}分。这表明您的情绪状况在冥想训练后有所改善，您可能已经感受到了自己的情绪更加稳定和积极，但请您坚持学习训练，您还有很大的提升的空间。"
        val AFTERTEST_EMOTION_75_90 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的后测情绪值为${num}分，训练效果得分为${num2}分。这说明您的冥想训练成效显著，情绪状况已得到大幅度改善。在这种状态下，您可能发现自己的心境更加平和，对生活充满乐观和积极的态度。"
        val AFTERTEST_EMOTION_90 =
            "<font color=\"#8A8989\"><b>·情绪值：</b></font>您的后测情绪值为${num}分 ，训练效果得分为${num2}分 。这是非常理想的结果，表示您的情绪状态通过冥想训练得到了显著的提升。在这种状态下，您可能发现自己无论在工作还是在学习中，都能以更积极、乐观的心态面对。我们建议您分享您的方式冥想体验与技巧给他人，并保持当前的冥想实践，继续提升情绪状态和生活质量。"

        if (num2 <= 40) {
            return AFTERTEST_EMOTION_40
        }
        if (num2 in 41..55) {
            return AFTERTEST_EMOTION_40_55
        }
        if (num2 in 56..75) {
            return AFTERTEST_EMOTION_55_75
        }
        if (num2 in 76..90) {
            return AFTERTEST_EMOTION_75_90
        }
        if (num2 > 90) {
            return AFTERTEST_EMOTION_90
        }
        return ""
    }

    fun aftertest_pressure(num: Int,num2: Int): String {
        val AFTERTEST_PRESSURE_40 =
            "<font color=\"#8A8989\"><b>·压力值：</b></font>在放松冥想训练的后测中，您的压力值为${num}分，训练效果得分为${num2}分，这可能意味着放松冥想训练对您的压力缓解效果并没有显著表现。如果您仍处在高压力水平，请您控制好内外因素后再次尝试，也可以尝试其他的放松训练方法。"
        val AFTERTEST_PRESSURE_40_55 =
            "<font color=\"#8A8989\"><b>·压力值：</b></font>在放松冥想训练的后测中，您的压力值为${num}分，训练效果得分为${num2}分。虽然您已经开始放松冥想训练，但其对压力缓解的影响可能还不太明显。您可以继续持续机型放松冥想训练，或者选择更加适合您的课程进行学习。"
        val AFTERTEST_PRESSURE_55_75 =
            "<font color=\"#8A8989\"><b>·压力值：</b></font>在放松冥想训练的后测中，您的压力值为${num}分，训练效果得分为${num2}分。这显示放松冥想训练正在帮助您缓解压力，但仍有进步的空间，持续的训练能够帮助您进一步缓解压力。"
        val AFTERTEST_PRESSURE_75_90 =
            "<font color=\"#8A8989\"><b>·压力值：</b></font>在放松冥想训练的后测中，您的压力值为${num}分，训练效果得分为${num2}分。这显示您通过冥想训练，已经成功地减轻了自己的压力。在此状态下，您可能发现自己在面对日常生活中的压力源时更加从容，更能保持平和的心态。"
        val AFTERTEST_PRESSURE_90 =
            "<font color=\"#8A8989\"><b>·压力值：</b></font>在放松冥想训练的后测中分，您的压力值为${num}分，训练效果得分为${num2}分。这是非常理想的结果，表示您的压力水平已经通过冥想训练得到了显著的缓解。在这种状态下，您可能发现自己无论在工作还是在学习中，都能以更平静的心态面对压力。"

        if (num2 <= 40) {
            return AFTERTEST_PRESSURE_40
        }
        if (num2 in 41..55) {
            return AFTERTEST_PRESSURE_40_55
        }
        if (num2 in 56..75) {
            return AFTERTEST_PRESSURE_55_75
        }
        if (num2 in 76..90) {
            return AFTERTEST_PRESSURE_75_90
        }
        if (num2 > 90) {
            return AFTERTEST_PRESSURE_90
        }
        return ""
    }
}