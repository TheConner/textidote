/*
    TeXtidote, a linter for LaTeX documents
    Copyright (C) 2018-2019  Sylvain Hallé

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package ca.uqac.lif.textidote.render;

import ca.uqac.lif.textidote.Advice;
import ca.uqac.lif.textidote.AdviceRenderer;
import ca.uqac.lif.textidote.as.PositionRange;
import ca.uqac.lif.util.AnsiPrinter;

import java.util.List;
import java.util.Map;

/**
 * Renders a list of advice to a terminal, uses formatting that is understood
 * by GitHub Actions
 * @see <a href="https://docs.github.com/en/actions/using-workflows/workflow-commands-for-github-actions">https://docs.github.com/en/actions/using-workflows/workflow-commands-for-github-actions</a>
 * @author Conner Bradley
 */
public class WorkflowAdviceRenderer extends AdviceRenderer
{
    /**
     * Creates a new advice renderer
     * @param printer The printer to which the advice will be printed
     */
    public WorkflowAdviceRenderer(AnsiPrinter printer)
    {
        super(printer);
    }

    @Override
    public void render()
    {
        for (Map.Entry<String,List<Advice>> entry : m_advice.entrySet())
        {
            String filename = entry.getKey();
            List<Advice> list = entry.getValue();

            if (list.isEmpty()) continue;

            for (Advice ad : list)
            {
                PositionRange pr = ad.getPositionRange();
                String annotationTitle = String.format("%s: [%s]", ad.getShortMessage(), ad.getRule().getName());
                m_printer.println(String.format("::warning file=%s,line=%d,col=%d,endLine=%d,endColumn=%d,title=%s::%s",
                        filename,
                        // Positions are 0 relative, github's lines are 1 relative
                        pr.getStart().getLine() + 1,
                        pr.getStart().getColumn() + 1,
                        pr.getEnd().getLine() + 1,
                        pr.getEnd().getColumn() + 1,
                        annotationTitle,
                        ad.getMessage()
                ));
            }
        }
    }
}